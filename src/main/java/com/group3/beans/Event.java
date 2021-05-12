package com.group3.beans;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

@Component
@Table("events")
public class Event {

	public enum Type {
		DOUBLESTRINGS, ROLLMOD;
	}

	private static int stringMod = 1;
	private static double rollMod = 1.0d;

	@Column
	@PrimaryKey
	private UUID eventId;

	@Column
	private Type eventType;

	@Column
	private boolean ongoing;

	@Column
	private Date eventStart;

	@Column
	private Date eventEnd;
	
	public Event() {
		super();

	}

	public static int getStringMod() {
		return stringMod;
	}

	public static void setStringMod(int stringMod) {
		Event.stringMod = stringMod;
	}

	public static double getRollMod() {
		return rollMod;
	}

	public static void setRollMod(double rollMod) {
		Event.rollMod = rollMod;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public Type getEventType() {
		return eventType;
	}

	public void setEventType(Type eventType) {
		this.eventType = eventType;
	}

	public boolean isOngoing() {
		return ongoing;
	}

	public void setOngoing(boolean ongoing) {
		this.ongoing = ongoing;
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
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		result = prime * result + ((eventStart == null) ? 0 : eventStart.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + (ongoing ? 1231 : 1237);
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
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		if (eventStart == null) {
			if (other.eventStart != null)
				return false;
		} else if (!eventStart.equals(other.eventStart))
			return false;
		if (eventType != other.eventType)
			return false;
		if (ongoing != other.ongoing)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", eventType=" + eventType + ", ongoing=" + ongoing + ", eventStart="
				+ eventStart + ", eventEnd=" + eventEnd + "]";
	}

	
}