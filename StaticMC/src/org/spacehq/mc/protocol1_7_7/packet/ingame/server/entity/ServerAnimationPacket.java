package org.spacehq.mc.protocol1_7_7.packet.ingame.server.entity;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerAnimationPacket implements Packet {
	
	private int entityId;
	private Animation animation;
	
	@SuppressWarnings("unused")
	private ServerAnimationPacket() {
	}
	
	public ServerAnimationPacket(int entityId, Animation animation) {
		this.entityId = entityId;
		this.animation = animation;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.animation = Animation.values()[in.readByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(this.animation.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Animation {
		SWING_ARM,
		DAMAGE,
		LEAVE_BED,
		EAT_FOOD,
		CRITICAL_HIT,
		ENCHANTMENT_CRITICAL_HIT;
	}

}
