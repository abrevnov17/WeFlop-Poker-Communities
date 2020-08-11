package com.weflop.GameService.Networking;

public enum MessageType {
	ACTION("ACTION"), 
	GAME_STATE("GAME_STATE"), 
	SYNCHRONIZATION("SYNCHRONIZATION");

	private final String value;

	MessageType(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}

	public static MessageType getTypeFromInt(int type) {
		return MessageType.values()[type];
	}
	
	public static MessageType fromValue(String value) {  
		if (value != null) {  
			for (MessageType type : values()) {  
				if (type.value.equals(value)) {  
					return type;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid message type: " + value);  
	}
}
