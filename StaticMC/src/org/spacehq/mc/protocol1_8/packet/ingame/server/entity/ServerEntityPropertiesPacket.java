package org.spacehq.mc.protocol1_8.packet.ingame.server.entity;

import org.spacehq.mc.protocol1_8.data.game.attribute.Attribute;
import org.spacehq.mc.protocol1_8.data.game.attribute.AttributeModifier;
import org.spacehq.mc.protocol1_8.data.game.values.MagicValues;
import org.spacehq.mc.protocol1_8.data.game.values.entity.AttributeType;
import org.spacehq.mc.protocol1_8.data.game.values.entity.ModifierOperation;
import org.spacehq.mc.protocol1_8.data.game.values.entity.ModifierType;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerEntityPropertiesPacket implements Packet {

	private int entityId;
	private List<Attribute> attributes;

	@SuppressWarnings("unused")
	private ServerEntityPropertiesPacket() {
	}

	public ServerEntityPropertiesPacket(int entityId, List<Attribute> attributes) {
		this.entityId = entityId;
		this.attributes = attributes;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public List<Attribute> getAttributes() {
		return this.attributes;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.attributes = new ArrayList<Attribute>();
		int length = in.readInt();
		for(int index = 0; index < length; index++) {
			String key = in.readString();
			double value = in.readDouble();
			List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
			int len = in.readVarInt();
			for(int ind = 0; ind < len; ind++) {
				modifiers.add(new AttributeModifier(MagicValues.key(ModifierType.class, in.readUUID()), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
			}

			this.attributes.add(new Attribute(MagicValues.key(AttributeType.class, key), value, modifiers));
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeVarInt(this.attributes.size());
		for(Attribute attribute : this.attributes) {
			out.writeString(MagicValues.value(String.class, attribute.getType()));
			out.writeDouble(attribute.getValue());
			out.writeShort(attribute.getModifiers().size());
			for(AttributeModifier modifier : attribute.getModifiers()) {
				UUID uuid = MagicValues.value(UUID.class, modifier.getType());
				out.writeUUID(uuid);
				out.writeDouble(modifier.getAmount());
				out.writeByte(MagicValues.value(Integer.class, modifier.getOperation()));
			}
		}
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
