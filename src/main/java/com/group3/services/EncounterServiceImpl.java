package com.group3.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.Event;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;
import com.group3.data.GamerRepository;
import com.group3.data.RewardTokenRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EncounterServiceImpl implements EncounterService {
	private Logger log = LoggerFactory.getLogger(EncounterServiceImpl.class);
	@Autowired
	private CollectibleService collectibleService;
	@Autowired
	private EncounterRepository encounterRepo;
	@Autowired
	private RewardTokenRepository rewardRepo;
	@Autowired
	private CollectibleRepository collectibleRepo;
	@Autowired
	private GamerRepository gamerRepo;
	@Autowired
	private EmailService emailService;

	public EncounterServiceImpl() {
		super();
	}

	public Flux<RewardToken> getRunningEncounters(UUID gamerID) {
		return rewardRepo.findAllByGamerID(gamerID);
	}

	public Mono<RewardToken> setEncounter(UUID gamerID, List<UUID> colIDs, UUID encID) {
		// Get the collectibles to send on the journey
		List<Collectible> sent = new ArrayList<>();
		for (UUID collectibleId : colIDs) {
			Collectible collectible = collectibleService.getCollectible(collectibleId.toString()).block();
			if (collectible != null) {
				sent.add(collectible);
			}
		}
		// If any of the collectibles sent
		// are on an encounter
		// or owned by a different gamer
		for (Collectible collectible : sent) {
			if (collectible.isOnEncounter() || !collectible.getGamerId().equals(gamerID)) {
				log.debug("Collectible list checkpoint failed in encounter service: "
						+ "collectible on encounter or gamerId does not match");
				return Mono.empty();
			}
		}

		RewardToken rewardToken = new RewardToken();
		rewardToken.setTokenID(Uuids.timeBased());
		rewardToken.setGamerID(gamerID);
		rewardToken.setActiveEncounter(encID);
		rewardToken.setCollectiblesOnEncounter(colIDs);
		// Get the journey to send on the collectibles
		encounterRepo.findByEncounterID(encID).doOnNext(e -> rewardToken.setReward(runEncounter(sent, e)))
				.doOnNext(e -> rewardToken.setEncounterTimes(e.getLength())).block();
		// Set the collectibles as unavailable to go on further encounters
		for (Collectible collectible : sent) {
			collectible.setOnEncounter(true);
			collectibleRepo.save(collectible).block();
		}
		return rewardRepo.insert(rewardToken);
	}

	public int runEncounter(List<Collectible> sent, Encounter journey) {
		// The deterministic version of the encounter runner.
		// A random version can be made later. (this is simpler to test)

		// Gather stat total of sent collectible list
		// TODO: Create a better way of calculating 'success'
		int total = sent.stream().mapToInt(x -> x.getCurrentStat()).sum();
		int reward;

		// If the combined statistical score of each collectible, reaches the set
		// Encounter difficulty, it wins and returns with a reward.
		if (total >= journey.getDifficulty()) {
			// Reward is defined by the encounter and limited by the number
			// of collectibles you send on the journey
			reward = journey.getReward() / sent.size();
		} else {
			// Return a small amount upon failure
			reward = 10;
		}
		return reward;
	}

	@Override
	public Flux<RewardToken> viewCompletedTokens(boolean encounterComplete) {
		return rewardRepo.findAllByEncounterComplete(false);
	}

	@Override
	public Mono<RewardToken> updateRewardToken(RewardToken token) {
		return rewardRepo.save(token);
	}

	// Constructor for testing
	public EncounterServiceImpl(CollectibleRepository cMock, EncounterRepository eMock, GamerRepository gMock) {
		this.collectibleRepo = cMock;
		this.encounterRepo = eMock;
		this.gamerRepo = gMock;
	}

	@Override
	public Mono<Encounter> createEncounterTemplate(Encounter encounter) {
		encounter.setEncounterID(Uuids.timeBased());
		return encounterRepo.insert(encounter);
	}

	@Override
	public Mono<Encounter> updateEncounterTemplate(Encounter encounter) {
		return encounterRepo.save(encounter);
	}

	@Override
	public Mono<Void> deleteEncounterTemplate(UUID encounter) {
		return encounterRepo.deleteById(encounter);
	}

	@Override
	public Mono<Gamer> distributeReward(int reward, UUID gamerID) {
		return gamerRepo.findById(gamerID).flatMap(gamer -> {
			if (gamer == null) {
				return Mono.empty();
			}
			if (reward < 20) {
				gamer.setStrings(gamer.getStrings() + (reward * 10 * Event.getStringMod()));
				log.debug("Reward distributed: {} strings", (reward * 10));
				emailService.sendEmail(gamer.getEmail(), "GachaGame: Encounter Complete!",
						"The encounter your collectibles went on is complete! " + "You received " + (reward * 10)
								+ " strings as a reward!");
			} else if (reward < 40) {
				gamer.setStardust(gamer.getStardust() + (reward / 10));
				log.debug("Reward distributed: {} stardust", (reward / 10));
				emailService.sendEmail(gamer.getEmail(), "GachaGame: Encounter Complete!",
						"The encounter your collectibles went on is complete! " + "You received " + (reward / 10)
								+ " stardust as a reward!");
			} else if (reward < 60) {
				gamer.setRolls(gamer.getRolls() + 1);
				log.debug("Reward distributed: 1 roll");
				emailService.sendEmail(gamer.getEmail(), "GachaGame: Encounter Complete!",
						"The encounter your collectibles went on is complete! "
								+ "You received 1 free roll as a reward!");
			} else if (reward < 80) {
				gamer.setRolls(gamer.getRolls() + 3);
				log.debug("Reward distributed: 3 rolls");
				emailService.sendEmail(gamer.getEmail(), "GachaGame: Encounter Complete!",
						"The encounter your collectibles went on is complete! "
								+ "You received 3 free rolls as a reward!");
			} else if (reward < 100) {
				gamer.setRolls(gamer.getRolls() + 5);
				log.debug("Reward distributed: 5 rolls! Nice.");
				emailService.sendEmail(gamer.getEmail(), "GachaGame: Encounter Complete!",
						"The encounter your collectibles went on is complete! "
								+ "You received 5 free rolls as a reward! Great work!");
			}

			return gamerRepo.save(gamer);
		});
	}


	@Override
	public Flux<Encounter> getEncounters(UUID uuid) {

		Gamer gamer = gamerRepo.findById(uuid).block();
		
		return encounterRepo.findAll()
			.filter(encounter -> encounter.getLevel() <= gamer.getCollectionStrength());
	}


}