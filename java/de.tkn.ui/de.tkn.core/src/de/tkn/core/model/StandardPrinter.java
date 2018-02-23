package de.tkn.core.model;

import de.tkn.core.api.ConsolePrinter;

public class StandardPrinter<S, T> implements ConsolePrinter<S, T>{

	@Override
	public void guardDetect(final S sender, final T object) {
		System.out.println("process\t" + object.toString() + "\tfrom\t" + sender.toString());		
	}

	@Override
	public void selectObject(final T object) {
		System.out.println("win\t" + object.toString());		
	}

	@Override
	public void guardError(final S sender) {
		System.err.println("unknown\t" + sender.toString());
	}
}
