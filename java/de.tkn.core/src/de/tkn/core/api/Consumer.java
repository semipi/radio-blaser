package de.tkn.core.api;

@FunctionalInterface
public interface Consumer<S, T> {

	/**
	 * 
	 * @param s
	 * @param t
	 */
	public void add(S s, T t);
}
