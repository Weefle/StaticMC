package org.spacehq.mc.protocol1_7_7.packet.ingame.server.window;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerCloseWindowPacket implements Packet {
	
	private int windowId;
	
	@SuppressWarnings("unused")
	private ServerCloseWindowPacket() {
	}
	
	public ServerCloseWindowPacket(int windowId) {
		this.windowId = windowId;
	}
	
	public int getWindowId() {
		return this.windowId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readUnsignedByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
