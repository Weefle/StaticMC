package org.spacehq.mc.protocol1_8.packet.ingame.client.player;

public class ClientPlayerPositionRotationPacket extends ClientPlayerMovementPacket {

	protected ClientPlayerPositionRotationPacket() {
		this.pos = true;
		this.rot = true;
	}

	public ClientPlayerPositionRotationPacket(boolean onGround, double x, double y, double z, float yaw, float pitch) {
		super(onGround);
		this.pos = true;
		this.rot = true;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

}
