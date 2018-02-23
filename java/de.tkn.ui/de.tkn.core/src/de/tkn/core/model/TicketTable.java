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

	@Override
	public Ticket lock(final Ticket t) {
		assert t != null;
		
		final Integer id = t.getId();
		
		if(receivedMsgs.containsKey(id)) {
			receivedMsgs.get(id).lock();
		}
		
		return t;
	}
	
	@Override
	public boolean isLocked(final Ticket t) {
		assert t != null;
		
		final Integer id = t.getId();
		
		if(receivedMsgs.containsKey(id)) {
			return receivedMsgs.get(id).locked;
		}
		
		return false;
	}

	private static class TicketInternal implements Ticket {
		
		private final Integer id;
		
		private int weight = 0; 
		
		private double signal = 0.0d;
		
		private boolean locked = false;
		
		private TicketInternal(final Integer id) {
			this.id = id;
		}

		private TicketInternal addValue(final int signal) {
			if(!locked) {
				final double weighted = weight++ * this.signal;
				this.signal = (weighted + signal) / weight;
			}
			return this;
		}
		
		public TicketInternal lock() {
			locked = true;
			return this;
		}
		
		@Override
		public String toString() {
			return String.valueOf(id) + "\t" + String.valueOf(signal);
		}
		
		@Override
		public Integer getId() {
			return id;
		}
		

		@Override
		public int compareTo(final Ticket o) {
			assert o != null;
			
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

		@Override
		public boolean equals(final Object obj) {
			if(obj == null) {
				return false;
			}
			if(this == obj) {
				return true;
			}
			if(getClass() != obj.getClass()) {
				return false;
			}
			
			return id == ((TicketInternal) obj).id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
	}

	@Override
	public void clear() {
		receivedMsgs.clear();
	}
}
