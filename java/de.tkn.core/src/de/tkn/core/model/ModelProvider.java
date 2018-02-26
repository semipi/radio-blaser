package de.tkn.core.model;

public interface ModelProvider<T extends Comparable<T>> {
	
	/**
	 * Adds T to the model provider.
	 * @param t
	 * @return
	 */
	public ModelProvider<T> add(final T t);
	
	/**
	 * Clears the model provider.
	 * @return
	 */
	public ModelProvider<T> clear();
	
	/**
	 * Returns the number of models.
	 * @return
	 */
	public int size();
}
