package com.group3.beans;

public class CombinedCollectible {
	
	private Collectible collectible;
	private CollectibleType type;

	public CombinedCollectible(Collectible collectible, CollectibleType type) {
		this.collectible = collectible;
		this.type = type;
	}

	public Collectible getCollectible() {
		return collectible;
	}

	public void setCollectible(Collectible collectible) {
		this.collectible = collectible;
	}

	@Override
	public String toString() {
		return "CombinedCollectible [collectible=" + collectible + ", type=" + type + "]";
	}

	public CollectibleType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collectible == null) ? 0 : collectible.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		CombinedCollectible other = (CombinedCollectible) obj;
		if (collectible == null) {
			if (other.collectible != null)
				return false;
		} else if (!collectible.equals(other.collectible))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public void setType(CollectibleType type) {
		this.type = type;
	}

}
