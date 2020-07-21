package com.weflop.GameService.Networking;

public enum MessageType {
	ACTION(0), GAME_STATE(1), SYNCHRONIZATION(2);

	private final int value;

	MessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MessageType getTypeFromInt(int type) {
		return MessageType.values()[type];
	}
}
