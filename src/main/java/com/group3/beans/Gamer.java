package com.group3.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table("gamers")
@Component
@JsonIgnoreProperties(value = { "password" })
public class Gamer implements Serializable, UserDetails {
	private static final long serialVersionUID = 4447548260627752098L;

	public enum Role implements GrantedAuthority {
		GAMER, MODERATOR, BANNED;

		@Override
		public String getAuthority() {
			return name();
		}
	}

	@Column
	@PrimaryKey
	private UUID gamerId;
	@Column
	@JsonInclude(Include.NON_NULL)
	private String username;
	@Column
	@JsonInclude(Include.NON_NULL)
	private String password;
	@Column
	@JsonInclude(Include.NON_NULL)
	private Role role;
	@Column
	@JsonInclude(Include.NON_NULL)
	private List<Role> authorities;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int rolls;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int dailyRolls;
	// daily free rolls, gets reset to 10 for every user on new day

	@Column
	@JsonInclude(Include.NON_NULL)
	private int stardust;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int strings;
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean loginBonusCollected;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int collectionSize;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int collectionStrength;
	@Column
	@JsonInclude(Include.NON_NULL)
	private int pvpScore;
	@Column
	@JsonInclude(Include.NON_NULL)
	private Date registrationDate;
	@Column
	@JsonInclude(Include.NON_NULL)
	private Date lastLogin;
	@Column
	@JsonInclude(Include.NON_NULL)
	private Set<Date> banDates;
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean enabled;
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean accountNonLocked;
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean credentialsNonExpired;
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean accountNonExpired;


	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Gamer() {
		super();
	}

	public UUID getGamerId() {
		return gamerId;
	}

	public void setGamerId(UUID gamerId) {
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

	public boolean isLoginBonusCollected() {
		return loginBonusCollected;
	}

	public void setLoginBonusCollected(boolean loginBonusCollected) {
		this.loginBonusCollected = loginBonusCollected;
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

	public Set<Date> getBanDates() {
		return banDates;
	}

	public void setBanDates(Set<Date> banDates) {
		this.banDates = banDates;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accountNonExpired ? 1231 : 1237);
		result = prime * result + (accountNonLocked ? 1231 : 1237);
		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + ((banDates == null) ? 0 : banDates.hashCode());
		result = prime * result + collectionSize;
		result = prime * result + collectionStrength;
		result = prime * result + (credentialsNonExpired ? 1231 : 1237);
		result = prime * result + dailyRolls;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((gamerId == null) ? 0 : gamerId.hashCode());
		result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + (loginBonusCollected ? 1231 : 1237);
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
		if (accountNonExpired != other.accountNonExpired)
			return false;
		if (accountNonLocked != other.accountNonLocked)
			return false;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (banDates == null) {
			if (other.banDates != null)
				return false;
		} else if (!banDates.equals(other.banDates))
			return false;
		if (collectionSize != other.collectionSize)
			return false;
		if (collectionStrength != other.collectionStrength)
			return false;
		if (credentialsNonExpired != other.credentialsNonExpired)
			return false;
		if (dailyRolls != other.dailyRolls)
			return false;
		if (enabled != other.enabled)
			return false;
		if (gamerId == null) {
			if (other.gamerId != null)
				return false;
		} else if (!gamerId.equals(other.gamerId))
			return false;
		if (lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!lastLogin.equals(other.lastLogin))
			return false;
		if (loginBonusCollected != other.loginBonusCollected)
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

	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public String toString() {
		return "Gamer [gamerId=" + gamerId + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", authorities=" + authorities + ", rolls=" + rolls + ", dailyRolls=" + dailyRolls + ", stardust="
				+ stardust + ", strings=" + strings + ", loginBonusCollected=" + loginBonusCollected
				+ ", collectionSize=" + collectionSize + ", collectionStrength=" + collectionStrength + ", pvpScore="
				+ pvpScore + ", registrationDate=" + registrationDate + ", lastLogin=" + lastLogin + ", banDates="
				+ banDates + ", enabled=" + enabled + ", accountNonLocked=" + accountNonLocked
				+ ", credentialsNonExpired=" + credentialsNonExpired + ", accountNonExpired=" + accountNonExpired + "]";
	}

}
