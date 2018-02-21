package de.tkn.core.api.rssi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tkn.core.api.Heap;
import de.tkn.core.api.Table;
import de.tkn.core.api.Ticket;
import de.tkn.core.model.FixedHeap;
import de.tkn.core.model.RssiMsg;

public class Selector<T> {
	
	public static final int HEAP_SIZE = 6;

	private final Map<T, TrackingManager> managers = new HashMap<>();
	
	private final Map<Ticket, OccurenceCounter<T>> occurences = new HashMap<>();
	
	private final Table<Ticket> table;
	
	public Selector(final Table<Ticket> table) {
		this.table = table;
	}
	
	public void register(final T t) {
		managers.put(t, new TrackingManager(table));
	}
	
	public void unregister(final T t) {
		managers.remove(t);
	}
	
	public Ticket add(final T t, final RssiMsg message) {
		final TrackingManager manager = managers.get(t);
		final Ticket ticket = manager.add(message);
		
		if(ticket != null) {
			if(!occurences.containsKey(ticket)) {
				occurences.put(ticket, new OccurenceCounter<>());
			}
			
			final OccurenceCounter<T> counter = occurences.get(ticket);
			counter.occurences.add(t);
			
			final Set<T> keySet = managers.keySet();
			
			if(keySet.equals(counter.occurences)) {
				occurences.remove(ticket);
				table.lock(ticket);
				
				return ticket;
			}
		}
		
		return null;
	}
	
	
	private static class OccurenceCounter<T> {
		
		private final Set<T> occurences = new HashSet<>();
	}

	private static class TrackingManager {
		
		public static final int HEAP_SIZE = 6;
		
		private boolean receiveMode = false;
		
		private final Table<Ticket> table;
		
		private TrackingManager(final Table<Ticket> table) {
			this.table = table;
		}
		
		private Heap<Ticket> heap = new FixedHeap<Ticket>(HEAP_SIZE);
		
		public Ticket add(final RssiMsg message) {	
			final MessageType type = MessageType.get(message.get_state());
			
			if(type == MessageType.START) {
				heap = new FixedHeap<Ticket>(HEAP_SIZE);
				receiveMode = true;
			} else if(type == MessageType.END) {
				receiveMode = false;
				return heap.getMax();
				
			} else {
				if(!receiveMode) {
					return null;
				}
				
				final Ticket ticket = table.get(
						message.getSerialPacket().get_header_src(), 
						message.get_rssi());
				heap.insert(ticket);
			}
			
			return null;
		}
	}
}
