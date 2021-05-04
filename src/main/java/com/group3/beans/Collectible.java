package com.group3.beans;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("collectible")
public class Collectible implements Serializable {
	private static final long serialVersionUID = 4776899515170739873L;
	@PrimaryKeyColumn(
			name="stage",
			ordinal=0,
			type = PrimaryKeyType.PARTITIONED)
	private Stage stage;
	@PrimaryKeyColumn(
			name="collectible_id",
			ordinal=1,
			type = PrimaryKeyType.CLUSTERED,
			ordering = Ordering.DESCENDING)
	private Integer collectibleId;
	@PrimaryKeyColumn(
			name="name",
			ordinal=2,
			type = PrimaryKeyType.CLUSTERED,
			ordering = Ordering.DESCENDING)
	private String name;
	@Column
	private Integer prevStage;
	@Column
	private Integer nextStage;
	
	public enum Stage {
		STAGE_1, STAGE_2, STAGE_3
	}

	public Collectible() {
		super();
	}

}
