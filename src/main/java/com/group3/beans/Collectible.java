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
		return "Collectible [gamerId=" + gamerId + ", id=" + id + ", typeId=" + typeId + ", currentStat=" + currentStat
				+ ", currentStage=" + currentStage + ", onEncounter=" + onEncounter + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentStage == null) ? 0 : currentStage.hashCode());
		result = prime * result + currentStat;
		result = prime * result + ((gamerId == null) ? 0 : gamerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (onEncounter ? 1231 : 1237);
		result = prime * result + typeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collectible other = (Collectible) obj;
		if (currentStage != other.currentStage)
			return false;
		if (currentStat != other.currentStat)
			return false;
		if (gamerId == null) {
			if (other.gamerId != null)
				return false;
		} else if (!gamerId.equals(other.gamerId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (onEncounter != other.onEncounter)
			return false;
		if (typeId != other.typeId)
			return false;
		return true;
	}
	
	
}
