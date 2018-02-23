package de.tkn.core.api;

public interface Heap<T extends Comparable<T>> extends Iterable<T> {

	public T getMax();
	
	public T getMin();
	
	public boolean insert(T t);
}
