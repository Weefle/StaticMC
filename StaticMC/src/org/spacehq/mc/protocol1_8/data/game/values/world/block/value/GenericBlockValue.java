package org.spacehq.mc.protocol1_8.data.game.values.world.block.value;

public class GenericBlockValue implements BlockValue {

	private int value;

	public GenericBlockValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
