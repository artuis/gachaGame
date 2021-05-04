package com.group3.beans;

import java.io.Serializable;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("gamers")
public class Gamer implements Serializable {
	private static final long serialVersionUID = 4447548260627752098L;

	public static enum Role {
		GAMER, MODERATOR
	}

	@Column
	@PrimaryKey
	private int gamerId;
	@Column
	private Role role;
	@Column
	private int rolls;
	@Column
	private int stardust;
	@Column
	private int strings;
	@Column
	private int collectionSize;
	@Column
	private int collectionStrength;
	@Column
	private int pvpScore;

	public Gamer() {
		super();
	}

	public int getGamerId() {
		return gamerId;
	}

	public void setGamerId(int gamerId) {
		this.gamerId = gamerId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getRolls() {
		return rolls;
	}

	public void setRolls(int rolls) {
		this.rolls = rolls;
	}

	public int getStardust() {
		return stardust;
	}

	public void setStardust(int stardust) {
		this.stardust = stardust;
	}

	public int getStrings() {
		return strings;
	}

	public void setStrings(int strings) {
		this.strings = strings;
	}

	public int getCollectionSize() {
		return collectionSize;
	}

	public void setCollectionSize(int collectionSize) {
		this.collectionSize = collectionSize;
	}

	public int getCollectionStrength() {
		return collectionStrength;
	}

	public void setCollectionStrength(int collectionStrength) {
		this.collectionStrength = collectionStrength;
	}

	public int getPvpScore() {
		return pvpScore;
	}

	public void setPvpScore(int pvpScore) {
		this.pvpScore = pvpScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + collectionSize;
		result = prime * result + collectionStrength;
		result = prime * result + gamerId;
		result = prime * result + pvpScore;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + rolls;
		result = prime * result + stardust;
		result = prime * result + strings;
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
		Gamer other = (Gamer) obj;
		if (collectionSize != other.collectionSize)
			return false;
		if (collectionStrength != other.collectionStrength)
			return false;
		if (gamerId != other.gamerId)
			return false;
		if (pvpScore != other.pvpScore)
			return false;
		if (role != other.role)
			return false;
		if (rolls != other.rolls)
			return false;
		if (stardust != other.stardust)
			return false;
		if (strings != other.strings)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gamer [gamerId=" + gamerId + ", role=" + role + ", rolls=" + rolls + ", stardust=" + stardust
				+ ", strings=" + strings + ", collectionSize=" + collectionSize + ", collectionStrength="
				+ collectionStrength + ", pvpScore=" + pvpScore + "]";
	}

}
