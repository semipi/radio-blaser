package de.tkn.core.api;

public interface Ticket extends Comparable<Ticket> {

	public Integer getId();
	
	public double getAvgSignalStrength();
	
}
 