package de.tkn.core.api.rssi;

import java.util.HashMap;
import java.util.Map;

import de.tkn.core.api.ConsolePrinter;
import de.tkn.core.api.Heap;
import de.tkn.core.api.Table;
import de.tkn.core.model.FixedHeap;
import de.tkn.core.model.RssiMsg;
import de.tkn.core.model.StandardPrinter;

public class Selector<S, T extends Comparable<T>> {
	
	public static final int HEAP_SIZE = 6;
	
	public static final double THRESHOLD = -30.0d;
	
	private final Table<T> table;
	
	private final Map<S, STOcc<T>> registered = new HashMap<>();
	
	private final Map<T, CTOcc<T>> combined = new HashMap<>();
	
	private long mask = 0;
	
	private final ConsolePrinter<S, T> printer = new StandardPrinter<S, T>();
	
	public Selector(final Table<T> table) {
		this.table = table;
	}
	
	public void register(final S s) {
		if(!registered.containsKey(s)) {
			final long smask = 1 << registered.size();
			registered.put(s, new STOcc<>(table, smask));
			mask |= smask;
		}
	}
	
	public T add(final S s, final RssiMsg message) {
		if(!registered.containsKey(s)) {
			printer.guardError(s);
			return null;
		}
		
		final STOcc<T> sto = registered.get(s);
		final T t = sto.add(message);
		
		if(t != null) {
			if(!combined.containsKey(t)) {
				combined.put(t, new CTOcc<>());
			}
			
			final CTOcc<T> cto = combined.get(t);
			cto.mask |= sto.smask;
			
			printer.guardDetect(s, t);
			
			if((mask & cto.mask) == mask) {
				printer.selectObject(t);
				combined.remove(t);
				table.lock(t);
				return t;
			}
		} 
		
		return null;
	}
	
	private static class STOcc<T extends Comparable<T>> {
		
		private boolean receiveMode = false;
		
		private Heap<T> heap = new FixedHeap<T>(HEAP_SIZE);
		
		private final Table<T> table;
		
		private final long smask;
		
		private STOcc(final Table<T> table, final long smask) {
			this.table = table;
			this.smask = smask;
		}
		
		public T add(final RssiMsg message) {	
			final MessageType type = MessageType.get(message.get_state());
			
			if(type == MessageType.START) {
				heap = new FixedHeap<T>(HEAP_SIZE);
				receiveMode = true;
			} else if(type == MessageType.END) {
				receiveMode = false;
				return heap.getMax();
				
			} else {
				if(!receiveMode) {
					return null;
				}

				final T t = table.get(
						message.getSerialPacket().get_header_src(), 
						message.get_rssi());
				
				if(!table.isLocked(t)) {
					heap.insert(t);
				}
			}
			
			return null;
		}
	}
	
	private static class CTOcc<T> {
		
		private long mask = 0;
	}
}
