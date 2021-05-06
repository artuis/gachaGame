package com.group3.beans;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;
@Data
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
	private CharSequence rawPassword;
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

}