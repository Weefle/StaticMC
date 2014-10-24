package org.spacehq.mc.protocol1_8.packet.ingame.client.player;

public class ClientPlayerPositionPacket extends ClientPlayerMovementPacket {

	protected ClientPlayerPositionPacket() {
		this.pos = true;
	}

	public ClientPlayerPositionPacket(boolean onGround, double x, double y, double z) {
		super(onGround);
		this.pos = true;
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
