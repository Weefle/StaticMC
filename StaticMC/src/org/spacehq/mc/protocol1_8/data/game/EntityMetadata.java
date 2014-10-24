package org.spacehq.mc.protocol1_8.data.game;

import org.spacehq.mc.protocol1_8.data.game.values.entity.MetadataType;

public class EntityMetadata {

	private int id;
	private MetadataType type;
	private Object value;

	public EntityMetadata(int id, MetadataType type, Object value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public int getId() {
		return this.id;
	}

	public MetadataType getType() {
		return this.type;
	}

	public Object getValue() {
		return this.value;
	}

}
