package org.spacehq.mc.protocol1_7_7.data.status;

import java.awt.image.BufferedImage;

import org.spacehq.mc.protocol1_7_7.data.message.Message;

public class ServerStatusInfo {

	private VersionInfo version;
	private PlayerInfo players;
	private Message description;
	private BufferedImage icon;
	
	public ServerStatusInfo(VersionInfo version, PlayerInfo players, Message description, BufferedImage icon) {
		this.version = version;
		this.players = players;
		this.description = description;
		this.icon = icon;
	}
	
	public VersionInfo getVersionInfo() {
		return this.version;
	}
	
	public PlayerInfo getPlayerInfo() {
		return this.players;
	}
	
	public Message getDescription() {
		return this.description;
	}
	
	public BufferedImage getIcon() {
		return this.icon;
	}
	
}
