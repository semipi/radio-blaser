package de.tkn.core.api;

/**
 * For printing on the console and keep the main code neat.
 *
 * @param <S>
 * @param <T>
 */
public interface ConsolePrinter<S, T> {

	public void guardDetect(S sender, T object);
	
	public void selectObject(T object);
	
	public void guardError(S sender);
}
