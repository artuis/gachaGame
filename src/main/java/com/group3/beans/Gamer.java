package com.group3.beans;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("gamers")
public class Gamer implements Serializable {
	private static final long serialVersionUID = 4447548260627752098L;

	public enum Role {
		GAMER, MODERATOR
	}

	@Column
	@PrimaryKey
	private int gamerId;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private Role role;
	@Column
	private int rolls;
	@Column
	private int dailyRolls;
	// daily free rolls, gets reset to 10 for every user on new day
	
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
	@Column
	private Date registrationDate;
	@Column
	private Date lastLogin;

	public Gamer() {
		super();
	}

	public int getGamerId() {
		return gamerId;
	}

	public void setGamerId(int gamerId) {
		this.gamerId = gamerId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public int getDailyRolls() {
		return dailyRolls;
	}

	public void setDailyRolls(int dailyRolls) {
		this.dailyRolls = dailyRolls;
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + collectionSize;
		result = prime * result + collectionStrength;
		result = prime * result + dailyRolls;
		result = prime * result + gamerId;
		result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + pvpScore;
		result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + rolls;
		result = prime * result + stardust;
		result = prime * result + strings;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		if (dailyRolls != other.dailyRolls)
			return false;
		if (gamerId != other.gamerId)
			return false;
		if (lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!lastLogin.equals(other.lastLogin))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (pvpScore != other.pvpScore)
			return false;
		if (registrationDate == null) {
			if (other.registrationDate != null)
				return false;
		} else if (!registrationDate.equals(other.registrationDate))
			return false;
		if (role != other.role)
			return false;
		if (rolls != other.rolls)
			return false;
		if (stardust != other.stardust)
			return false;
		if (strings != other.strings)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gamer [gamerId=" + gamerId + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", rolls=" + rolls + ", dailyRolls=" + dailyRolls + ", stardust=" + stardust + ", strings=" + strings
				+ ", collectionSize=" + collectionSize + ", collectionStrength=" + collectionStrength + ", pvpScore="
				+ pvpScore + ", registrationDate=" + registrationDate + ", lastLogin=" + lastLogin + "]";
	}

	