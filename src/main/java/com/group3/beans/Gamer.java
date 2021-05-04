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
	private double gamerId;
	@Column
	private Role role;
	@Column
	private double rolls;
	@Column
	private double stardust;
	@Column
	private double strings;
	@Column
	private double collectionSize;
	@Column
	private double collectionStrength;
	@Column
	private double pvpScore;

	public Gamer() {
		super();
	}

	public double getGamerId() {
		return gamerId;
	}

	public void setGamerId(double gamerId) {
		this.gamerId = gamerId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public double getRolls() {
		return rolls;
	}

	public void setRolls(double rolls) {
		this.rolls = rolls;
	}

	public double getStardust() {
		return stardust;
	}

	public void setStardust(double stardust) {
		this.stardust = stardust;
	}

	public double getStrings() {
		return strings;
	}

	public void setStrings(double strings) {
		this.strings = strings;
	}

	public double getCollectionSize() {
		return collectionSize;
	}

	public void setCollectionSize(double collectionSize) {
		this.collectionSize = collectionSize;
	}

	public double getCollectionStrength() {
		return collectionStrength;
	}

	public void setCollectionStrength(double collectionStrength) {
		this.collectionStrength = collectionStrength;
	}

	public double getPvpScore() {
		return pvpScore;
	}

	public void setPvpScore(double pvpScore) {
		this.pvpScore = pvpScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(collectionSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(collectionStrength);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(gamerId);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pvpScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		temp = Double.doubleToLongBits(rolls);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stardust);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(strings);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(collectionSize) != Double.doubleToLongBits(other.collectionSize))
			return false;
		if (Double.doubleToLongBits(collectionStrength) != Double.doubleToLongBits(other.collectionStrength))
			return false;
		if (Double.doubleToLongBits(gamerId) != Double.doubleToLongBits(other.gamerId))
			return false;
		if (Double.doubleToLongBits(pvpScore) != Double.doubleToLongBits(other.pvpScore))
			return false;
		if (role != other.role)
			return false;
		if (Double.doubleToLongBits(rolls) != Double.doubleToLongBits(other.rolls))
			return false;
		if (Double.doubleToLongBits(stardust) != Double.doubleToLongBits(other.stardust))
			return false;
		if (Double.doubleToLongBits(strings) != Double.doubleToLongBits(other.strings))
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
