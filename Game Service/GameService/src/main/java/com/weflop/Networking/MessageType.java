package com.weflop.Networking;

public enum MessageType {
	JOIN_GAME(0),
	ACTION(1);
	
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
