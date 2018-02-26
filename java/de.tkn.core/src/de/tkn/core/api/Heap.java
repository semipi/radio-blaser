package de.tkn.core.api;

/**
 * Can be implemented of (fixed) heaps.
 * @param <T>
 */
public interface Heap<T extends Comparable<T>> extends Iterable<T> {

	/**
	 * @return maximum of the heap.
	 */
	public T getMax();
	
	/**
	 * @return minimum of the heap.
	 */
	public T getMin();
	
	/**
	 * Inserts an object.
	 * @param t
	 * @return <code>true</code> if inserted.
	 */
	public boolean insert(T t);
}
