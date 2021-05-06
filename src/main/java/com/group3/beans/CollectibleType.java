package com.group3.beans;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("collectibleTypes")
public class CollectibleType implements Serializable {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
