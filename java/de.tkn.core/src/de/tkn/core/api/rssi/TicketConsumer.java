package de.tkn.core.api.rssi;

import java.util.HashSet;
import java.util.Set;

import de.tkn.core.api.Command;
import de.tkn.core.api.Consumer;
import de.tkn.core.api.Heap;
import de.tkn.core.api.Table;
import de.tkn.core.api.Ticket;
import de.tkn.core.model.FixedHeap;
import de.tkn.core.model.RssiMsg;
import de.tkn.core.model.TicketTable;

public class TicketConsumer implements Consumer<RssiMsg> {
	
	public static final int HEAP_SIZE = 6;

	private MessageType type = MessageType.NONE;
	
	private boolean receiveMode = false;
	
	private final Table<Ticket> table = new TicketTable();
	
	private Heap<Ticket> heap = new FixedHeap<Ticket>(HEAP_SIZE);
	
	private final Set<Command<Ticket>> listener = new HashSet<>(); 
	
	public TicketConsumer() {
	}
	
	public boolean register(final Command<Ticket> command) {
		return listener.add(command);
	}
	
	@Override
	public void add(final RssiMsg message) {	
		type = MessageType.get(message.get_state());
		
		if(type == MessageType.START) {
			heap = new FixedHeap<Ticket>(HEAP_SIZE);
			receiveMode = true;
		} else if(type == MessageType.END) {
			receiveMode = false;
			final Ticket max = heap.getMax();
			
			if(max != null) {
				table.lock(max);
				listener.forEach(c -> c.exec(max));
			}
		} else {
			if(!receiveMode) {
				return;
			}
			
			final Ticket ticket = table.get(
					message.getSerialPacket().get_header_src(), 
					message.get_rssi());
			heap.insert(ticket);
		}
	}
}
