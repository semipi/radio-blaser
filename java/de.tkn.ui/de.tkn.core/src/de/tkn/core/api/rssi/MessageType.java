package de.tkn.core.api.rssi;

public enum MessageType {
	NONE(0), START(1), END(2);
	
	private int value;
	
	private MessageType(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static MessageType get(final int value) {
		switch(value) {
		case 0: return NONE;
		case 1: return START;
		case 2: return END;
		default: return NONE;
		}
	}
}
