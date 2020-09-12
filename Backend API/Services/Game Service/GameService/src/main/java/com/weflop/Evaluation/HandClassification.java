package com.weflop.Evaluation;

/**
 * HandClassification.java
 * 
 * Enum representing all classifications of poker hands
 * 
 * @author abrevnov
 *
 */
public enum HandClassification {
	INVALID_HAND("INVALID_HAND"), 
	HIGH_CARD("HIGH_CARD"), 
	PAIR("PAIR"), 
	TWO_PAIR("TWO_PAIR"), 
	THREE_OF_A_KIND("THREE_OF_A_KIND"), 
	STRAIGHT("STRAIGHT"), 
	FLUSH("FLUSH"), 
	FULL_HOUSE("FULL_HOUSE"), 
	FOUR_OF_A_KIND("FOUR_OF_A_KIND"), 
	STRAIGHT_FLUSH("STRAIGHT_FLUSH");
	
	private final String value;

	HandClassification(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static HandClassification fromValue(String value) {  
		if (value != null) {  
			for (HandClassification classification : values()) {  
				if (classification.value.equals(value)) {  
					return classification;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid hand classification: " + value);  
	}
}
