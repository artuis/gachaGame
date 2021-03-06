package com.group3.beans;

import java.io.Serializable;
import java.util.UUID;


import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.CollectibleType.Stage;

@Component
@Table("collectibles")
public class Collectible implements Serializable{
	private static final long serialVersionUID = 4776899515170739873L;

	@PrimaryKeyColumn(name = "gamerId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID gamerId;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	@Column
	private int typeId; 
	@Column
	private int currentStat;
	@Column
	private Stage currentStage;
	@Column
	private boolean onEncounter;
	
	public Collectible() {
		super();
	}

	public static Collectible fromCollectibleTypeAndId(CollectibleType ct, UUID gamerId) {
		Collectible c = new Collectible();
		c.setId(Uuids.timeBased());
		c.setGamerId(gamerId);
		c.setCurrentStat(ct.getBaseStat());
		c.setTypeId(ct.getId());
		c.setCurrentStage(ct.getStage());
		return c;
	}

	public int getCurrentStat() {
		return currentStat;
	}

	public void setCurrentStat(int currentStat) {
		this.currentStat = currentStat;
	}

	public UUID getGamerId() {
		return gamerId;
	}

	public void setGamerId(UUID gamerId) {
		this.gamerId = gamerId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;

	}
	
	public Stage getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

	public boolean isOnEncounter() {
		return onEncounter;
	}

	public void setOnEncounter(boolean onEncounter) {
		this.onEncounter = onEncounter;
	}

	@Override
	public String toString() {
		return "Collectible [id=" + id.toString() + ", gamerId=" + gamerId.toString() + ", typeId=" + typeId + ", currentStat=" + currentStat
				+ "]";
	}
	
	
}
