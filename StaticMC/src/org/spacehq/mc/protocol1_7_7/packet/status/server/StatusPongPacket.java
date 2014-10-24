package org.spacehq.mc.protocol1_7_7.packet.status.server;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class StatusPongPacket implements Packet {
	
	private long time;
	
	@SuppressWarnings("unused")
	private StatusPongPacket() {
	}
	
	public StatusPongPacket(long time) {
		this.time = time;
	}
	
	public long getPingTime() {
		return this.time;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.time = in.readLong();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeLong(this.time);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
