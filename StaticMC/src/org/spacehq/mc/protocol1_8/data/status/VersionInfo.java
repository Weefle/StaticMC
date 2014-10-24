package org.spacehq.mc.protocol1_8.data.status;

public class VersionInfo {

	private String name;
	private int protocol;

	public VersionInfo(String name, int protocol) {
		this.name = name;
		this.protocol = protocol;
	}

	public String getVersionName() {
		return this.name;
	}

	public int getProtocolVersion() {
		return this.protocol;
	}

}
