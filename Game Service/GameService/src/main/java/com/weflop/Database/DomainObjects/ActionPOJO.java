package com.weflop.Database.DomainObjects;

/**
 * CRUD object wrapping information about user actions
 * 
 * @author abrevnov
 */
public class ActionPOJO {
	private int type;
	
	private String userId;
	
	private float value;
	
	public ActionPOJO(int type, String userId) {
        this.type = type;
        this.userId = userId;
    }

	public ActionPOJO(int type, String userId, float value) {
		this(type, userId);
		this.value = value;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
