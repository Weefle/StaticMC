package org.spacehq.mc.protocol1_7_7.packet.ingame.server.entity;

import java.io.IOException;

import org.spacehq.mc.protocol1_7_7.data.game.ItemStack;
import org.spacehq.mc.protocol1_7_7.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityEquipmentPacket implements Packet {
	
	private int entityId;
	private int slot;
	private ItemStack item;
	
	@SuppressWarnings("unused")
	private ServerEntityEquipmentPacket() {
	}
	
	public ServerEntityEquipmentPacket(int entityId, int slot, ItemStack item) {
		this.entityId = entityId;
		this.slot = slot;
		this.item = item;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.slot = in.readShort();
		this.item = NetUtil.readItem(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeShort(this.slot);
		NetUtil.writeItem(out, this.item);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
