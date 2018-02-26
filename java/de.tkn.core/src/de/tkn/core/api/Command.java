package de.tkn.core.api;

@FunctionalInterface
public interface Command<T> {

	/**
	 * Behavioral design pattern for commands.
	 * @param t
	 */
	public void exec(T t);
}
