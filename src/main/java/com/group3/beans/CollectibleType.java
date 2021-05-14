package com.group3.beans;

import java.io.Serializable;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
@Primary
@Table("collectibleTypes")
public class CollectibleType implements Serializable {
	@Override
	public String toString() {
		return "CollectibleType [stage=" + stage + ", id=" + id + ", name=" + name + ", prevStage=" + prevStage
				+ ", nextStage=" + nextStage + ", baseStat=" + baseStat + "]";
	}

	private static final long serialVersionUID = 4776899515170739873L;
	@PrimaryKeyColumn(name = "stage", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private Stage stage;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private Integer id;
	@PrimaryKeyColumn(name = "name", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String name;
	@Column
	private Integer prevStage;
	@Column
	private Integer nextStage;
	@Column
	private Integer baseStat;

	public enum Stage {
		STAGE_1(0.87), STAGE_2(0.98), STAGE_3(1.00);

		private final double rate;

		Stage(double rate) {
			this.rate = rate;
		}

		public double getRate() {
			return this.rate;
		}
	}

	public CollectibleType() {
		super();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrevStage() {
		return prevStage;
	}

	public void setPrevStage(Integer prevStage) {
		this.prevStage = prevStage;
	}

	public Integer getNextStage() {
		return nextStage;
	}

	public void setNextStage(Integer nextStage) {
		this.nextStage = nextStage;
	}

	public Integer getBaseStat() {
		return baseStat;
	}

	public void setBaseStat(Integer stat) {
		this.baseStat = stat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseStat == null) ? 0 : baseStat.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nextStage == null) ? 0 : nextStage.hashCode());
		result = prime * result + ((prevStage == null) ? 0 : prevStage.hashCode());
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
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
		CollectibleType other = (CollectibleType) obj;
		if (baseStat == null) {
			if (other.baseStat != null)
				return false;
		} else if (!baseStat.equals(other.baseStat))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nextStage == null) {
			if (other.nextStage != null)
				return false;
		} else if (!nextStage.equals(other.nextStage))
			return false;
		if (prevStage == null) {
			if (other.prevStage != null)
				return false;
		} else if (!prevStage.equals(other.prevStage))
			return false;
		if (stage != other.stage)
			return false;
		return true;
	}

}
