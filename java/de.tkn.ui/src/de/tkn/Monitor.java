package de.tkn;

import javax.swing.JFrame;

import de.tkn.ui.MonitorFrame;

/**
 * Main Application.
 *
 */
public class Monitor {

	public static void main(final String[] args) {
		final JFrame frame = MonitorFrame.create();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
