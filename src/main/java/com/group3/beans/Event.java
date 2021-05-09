package com.group3.beans;

import java.util.Date;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table("events")
public class Event {
	
	public enum Type {
		DOUBLESTRINGS, ROLLMOD;
	}
	@Column
	@PrimaryKey
	private UUID 
	
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private Type eventType;
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private boolean isOngoing;
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private Date eventStart;
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private Date eventEnd;

	public Event() {
		// TODO Auto-generated constructor stub
	}

	public Type getEventType() {
		return eventType;
	}

	public void setEventType(Type eventType) {
		this.eventType = eventType;
	}

	public boolean isOngoing() {
		return isOngoing;
	}

	public void setOngoing(boolean isOngoing) {
		this.isOngoing = isOngoing;
	}

	public Date getEventStart() {
		return eventStart;
	}

	public void setEventStart(Date eventStart) {
		this.eventStart = eventStart;
	}

	public Date getEventEnd() {
		return eventEnd;
	}

	public void setEventEnd(Date eventEnd) {
		this.eventEnd = eventEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventEnd == null) ? 0 : eventEnd.hashCode());
		result = prime * result + ((eventStart == null) ? 0 : eventStart.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + (isOngoing ? 1231 : 1237);
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
		Event other = (Event) obj;
		if (eventEnd == null) {
			if (other.eventEnd != null)
				return false;
		} else if (!eventEnd.equals(other.eventEnd))
			return false;
		if (eventStart == null) {
			if (other.eventStart != null)
				return false;
		} else if (!eventStart.equals(other.eventStart))
			return false;
		if (eventType != other.eventType)
			return false;
		if (isOngoing != other.isOngoing)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [eventType=" + eventType + ", isOngoing=" + isOngoing + ", eventStart=" + eventStart
				+ ", eventEnd=" + eventEnd + "]";
	}

}