package de.tkn;

import javax.swing.JFrame;

import de.tkn.ui.MonitorFrame;

public class Monitor {

	public static void main(final String[] args) {
		final JFrame frame = MonitorFrame.create();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//final RssiMessageTracker tracker = new RssiMessageTracker("-comm serial@/dev/ttyUSB1:telosb", new TicketProvider());
		 
//		final PhoenixSource phoenix = BuildSource.makePhoenix("serial@/dev/ttyUSB1:telosb", PrintStreamMessenger.err);
//		System.out.println(phoenix == null);
	}
}
