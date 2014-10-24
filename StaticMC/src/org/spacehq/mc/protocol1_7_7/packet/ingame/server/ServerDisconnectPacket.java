package org.spacehq.mc.protocol1_7_7.packet.ingame.server;

import java.io.IOException;

import org.spacehq.mc.protocol1_7_7.data.message.Message;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerDisconnectPacket implements Packet {
	
	private Message message;
	
	@SuppressWarnings("unused")
	private ServerDisconnectPacket() {
	}
	
	public ServerDisconnectPacket(String text) {
		this(Message.fromString(text));
	}
	
	public ServerDisconnectPacket(Message message) {
		this.message = message;
	}
	
	public Message getReason() {
		return this.message;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.message = Message.fromString(in.readString());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.message.toJsonString());
	}
	
	@Override
	public boolean isPriority() {
		return true;
	}

}
