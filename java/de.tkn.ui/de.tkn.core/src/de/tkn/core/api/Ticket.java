package de.tkn.core.api;

import java.util.Comparator;

public interface Ticket extends Comparable<Ticket> {
	
	public static final Comparator<Ticket> TICKET_COMPARATOR = (arg0, arg1) -> {
		if(arg0.getId() < arg1.getId()) {
			return -1;
		} else if(arg0.getId() > arg1.getId()) {
			return 1;
		} else {
			return 0;
		}
	};

	public Integer getId();
	
	public double getAvgSignalStrength();
	
}
 