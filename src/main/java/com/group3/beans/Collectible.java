package com.group3.beans;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("collectibles")
public class Collectible implements Serializable{
	private static final long serialVersionUID = 4776899515170739873L;
	@PrimaryKeyColumn(name = "gamerId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private int gamerId;

	@Column
	private int typeId;
	
	public int getGamerId() {
		return gamerId;
	}

	public void setGamerId(int gamerId) {
		this.gamerId = gamerId;
	}

	@Override
	public String toString() {
		return "Collectible [gamerId=" + gamerId + ", typeId=" + typeId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gamerId;
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
		if (gamerId != other.gamerId)
			return false;
		if (typeId != other.typeId)
			return false;
		return true;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
