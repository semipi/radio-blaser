package de.tkn.core.model;

public interface ModelProvider<T extends Comparable<T>> {
	
	public ModelProvider<T> add(final T t);
	
	public ModelProvider<T> clear();
	
	public int size();
}
