package de.tkn.core.model;

import java.util.HashMap;
import java.util.Map;

import de.tkn.core.api.Table;
import de.tkn.core.api.Ticket;

public class TicketTable implements Table<Ticket>{

	private final Map<Integer, TicketInternal> receivedMsgs = new HashMap<>();
	
	public TicketTable reset() {
		receivedMsgs.clear();
		return this;
	}
	
	@Override
	public Ticket get(final Integer id, final int signal) {
		if(!receivedMsgs.containsKey(id)) {
			receivedMsgs.put(id, new TicketInternal(id));
		}
		
		return receivedMsgs.get(id).addValue(signal);
	}

	private static class TicketInternal implements Ticket {
		
		private final Integer id;
		
		private int weight = 0; 
		
		private double signal = 0.0d;
		
		private TicketInternal(final Integer id) {
			this.id = id;
		}

		private TicketInternal addValue(final int signal) {
			final double weighted = weight++ * this.signal;
			this.signal = (weighted + signal) / weight;
			
			return this;
		}
		
		@Override
		public Integer getId() {
			return id;
		}
		

		@Override
		public int compareTo(final Ticket o) {
			if(signal < o.getAvgSignalStrength()) {
				return -1;
			}
			
			if(signal > o.getAvgSignalStrength()) {
				return 1;
			}
			
			return 0;
		}


		@Override
		public double getAvgSignalStrength() {
			return signal;
		}
	}
}
