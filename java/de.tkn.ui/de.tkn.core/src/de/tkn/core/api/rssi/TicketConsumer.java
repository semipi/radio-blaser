package de.tkn.core.api.rssi;

import java.util.HashSet;
import java.util.Set;

import de.tkn.core.api.Command;
import de.tkn.core.api.Consumer;
import de.tkn.core.api.Table;
import de.tkn.core.api.Ticket;
import de.tkn.core.model.RssiMsg;
import de.tkn.core.model.TicketTable;

public class TicketConsumer implements Consumer<String, RssiMsg> {

	private final Table<Ticket> table = new TicketTable();
	
	private final Selector<String, Ticket> selector = new Selector<>(table);
	
	private final Set<Command<Ticket>> listener = new HashSet<>();
	
	private final Set<RssiMessageTracker> trackers = new HashSet<>();
	
	public TicketConsumer() {
	}
	
	public void clear() {
		table.clear();
	}
	
	public boolean register(final Command<Ticket> command) {
		return listener.add(command);
	}
	
	public void createNewTracker(final String source) {
		final RssiMessageTracker tracker = new RssiMessageTracker(source, this);
		trackers.add(tracker);
		selector.register(tracker.toString());
	}
	
	@Override
	public void add(final String sender, final RssiMsg message) {
		final Ticket ticket = selector.add(sender, message);
		
		if(ticket != null) {
			listener.forEach(c -> c.exec(ticket));
		}
	}
}
