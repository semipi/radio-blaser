package de.tkn.core.api;

public interface ConsolePrinter<S, T> {

	public void guardDetect(S sender, T object);
	
	public void selectObject(T object);
	
	public void guardError(S sender);
}
