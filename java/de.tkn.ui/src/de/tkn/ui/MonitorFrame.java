package de.tkn.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import de.tkn.core.api.Ticket;
import de.tkn.core.api.rssi.RssiMessageTracker;
import de.tkn.core.api.rssi.TicketConsumer;

public class MonitorFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int COLUMN_ID = 0;
	private static final int COLUMN_AVG = 1;

	private RssiMessageTracker tracker;

	private final TicketConsumer consumer = new TicketConsumer();
	
	private MonitorFrame() {
		super("Radio-Blaser");
		setSize(400, 200);
		setVisible(true);
		
		final JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

		createHeader(main);		
		createConnectionLine(main);
		createTable(main);
		createFooter(main);
		
		add(main);
		pack();
	}
	
	private void createFooter(final JPanel parent) {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		parent.add(panel);
		
		final GridBagConstraints left = new GridBagConstraints();
		final JButton button = new JButton("Reset");
		button.addActionListener(a -> {
			
		});
		
		panel.add(button, left);
	}
	
	private void createHeader(final JPanel parent) {
		final JLabel header = new JLabel();
		header.setText("Monitor Tool");
		
		parent.add(header);
	}
	
	private void createTable(final JPanel parent) {
		final MonitorTableModel<Ticket> model = new MonitorTableModel<Ticket>(
				new String[] { "Node id", "Avg. signal strength" },
				(t, column) -> {
					if(t == null) {
						return null;
					}
					if(column == COLUMN_ID) {
						return String.valueOf(t.getId());
					} else if(column == COLUMN_AVG) {
						return String.valueOf(t.getAvgSignalStrength());
					} else {
						return null;
					}
				});
		
		final JTable table = new JTable(model);
		final JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.BLACK);
		
		consumer.register(t -> model.add(t));
		parent.add(scrollPane);
		
	}

	private void createConnectionLine(final JPanel parent) {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		parent.add(panel);

		final GridBagConstraints left = new GridBagConstraints();
		final JLabel label = new JLabel();
		label.setText("Enter Port: ");
		panel.add(label, left);

		final GridBagConstraints right = new GridBagConstraints();
		right.weightx = 2.0;
		right.fill = GridBagConstraints.HORIZONTAL;
		right.gridwidth = GridBagConstraints.REMAINDER;
		final JTextField input = new JTextField();
		panel.add(input, right);
		input.addActionListener(e -> {
			if(tracker != null) {
				tracker.tearDown();
			}
			tracker = new RssiMessageTracker(input.getText(), consumer);
		}); 
	}

	public static JFrame create() {
		return new MonitorFrame();
	}
}
