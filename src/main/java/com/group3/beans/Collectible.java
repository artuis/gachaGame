package com.group3.beans;

import java.io.Serializable;


import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("collectibles")
public class Collectible implements Serializable{
	private static final long serialVersionUID = 4776899515170739873L;

	@PrimaryKeyColumn(name = "gamerId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private int gamerId;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private int id;
	@Column
	private int typeId;
	@Column
	private int currentStat;
	
	public Collectible() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCurrentStat() {
		return currentStat;
	}

	public void setCurrentStat(int currentStat) {
		this.currentStat = currentStat;
	}

	public int getGamerId() {
		return gamerId;
	}

	public void setGamerId(int gamerId) {
		this.gamerId = gamerId;
	}

	@Override
	public String toString() {
		return "Collectible [id=" + id + ", gamerId=" + gamerId + ", typeId=" + typeId + ", currentStat=" + currentStat
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;

	}
}
