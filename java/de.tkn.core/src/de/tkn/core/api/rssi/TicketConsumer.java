package de.tkn.core.api.rssi;

import java.util.HashSet;
import java.util.Set;

import de.tkn.core.api.Command;
import de.tkn.core.api.Consumer;
import de.tkn.core.api.Table;
import de.tkn.core.api.Ticket;
import de.tkn.core.model.RssiMsg;
import de.tkn.core.model.TicketTable;

public class TicketConsumer implements Consumer<RssiMessageTracker, RssiMsg> {

	private final Table<Ticket> table = new TicketTable();
	
	private final Selector<RssiMessageTracker> selector = new Selector<>(table);
	
	private final Set<Command<Ticket>> listener = new HashSet<>(); 
	
	public TicketConsumer() {
	}
	
	public void update(final RssiMessageTracker oldTracker, final RssiMessageTracker newTracker) {
		selector.unregister(oldTracker);
		selector.register(newTracker);
	}
	
//	public void register(final RssiMessageTracker tracker) {
//		selector.register(tracker);
//	}
//	
//	public void unregister(final RssiMessageTracker tracker) {
//		selector.unregister(tracker);
//	}
	
	public boolean register(final Command<Ticket> command) {
		return listener.add(command);
	}
	
	@Override
	public void add(final RssiMessageTracker sender, final RssiMsg message) {	
		final Ticket ticket = selector.add(sender, message);
		
		if(ticket != null) {
			listener.forEach(c -> c.exec(ticket));
		}
		
//		final MessageType type = MessageType.get(message.get_state());
//		final Heap<Ticket> heap = selector.getHeap(sender, type);
//		
//		if(type == MessageType.START) {
//			heap = new FixedHeap<Ticket>(HEAP_SIZE);
//			receiveMode = true;
//		} else if(type == MessageType.END) {
//			receiveMode = false;
//			final Ticket max = heap.getMax();
//			
//			if(max != null) {
//				table.lock(max);
//				listener.forEach(c -> c.exec(max));
//			}
//		} else {
//			if(!receiveMode) {
//				return;
//			}
//			
//			final Ticket ticket = table.get(
//					message.getSerialPacket().get_header_src(), 
//					message.get_rssi());
//			heap.insert(ticket);
//		}
	}
	
	
}
