package de.tkn.core.api.rssi;

import de.tkn.core.api.Consumer;
import de.tkn.core.model.RssiMsg;
import net.tinyos.message.Message;
import net.tinyos.message.MessageListener;
import net.tinyos.message.MoteIF;
import net.tinyos.packet.BuildSource;
import net.tinyos.packet.PhoenixSource;
import net.tinyos.util.PrintStreamMessenger;

public class RssiMessageTracker implements MessageListener {
	
	private final MoteIF mif;
	
	private final Consumer<RssiMsg> consumer;
 
	public RssiMessageTracker(final String source, final Consumer<RssiMsg> consumer) {
		final PhoenixSource phoenix = BuildSource.makePhoenix("serial@" + source + ":telosb", PrintStreamMessenger.err);
		mif = new MoteIF(phoenix);
		mif.registerListener(new RssiMsg(), this);
		
		this.consumer = consumer;
	}

	@Override
	public void messageReceived(final int arg0, final Message arg1) {
		final RssiMsg message = (RssiMsg) arg1;
		consumer.add(message);
	}
	
	public void tearDown() {
		mif.deregisterListener(new RssiMsg(), this);
	}
}