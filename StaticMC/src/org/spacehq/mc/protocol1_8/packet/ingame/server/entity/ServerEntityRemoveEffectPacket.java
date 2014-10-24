package org.spacehq.mc.protocol1_8.packet.ingame.server.entity;

import org.spacehq.mc.protocol1_8.data.game.values.MagicValues;
import org.spacehq.mc.protocol1_8.data.game.values.entity.Effect;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityRemoveEffectPacket implements Packet {

	private int entityId;
	private Effect effect;

	@SuppressWarnings("unused")
	private ServerEntityRemoveEffectPacket() {
	}

	public ServerEntityRemoveEffectPacket(int entityId, Effect effect) {
		this.entityId = entityId;
		this.effect = effect;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public Effect getEffect() {
		return this.effect;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.effect = MagicValues.key(Effect.class, in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.effect));
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
