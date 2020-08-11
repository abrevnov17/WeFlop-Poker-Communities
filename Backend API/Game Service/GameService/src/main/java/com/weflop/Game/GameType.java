package com.weflop.Game;

/**
 * Enum describes all supported game types.
 * 
 * @author abrevnov
 *
 */
public enum GameType {
	STANDARD_REPRESENTATION("STANDARD_REPRESENTATION");

	private final String value;

	GameType(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static GameType fromValue(String value) {  
		if (value != null) {  
			for (GameType type : values()) {  
				if (type.value.equals(value)) {  
					return type;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid game type: " + value);  
	}
}
