package de.tkn.core.api;

@FunctionalInterface
public interface PeekStrategy<T extends Comparable<T>> {

	/**
	 * Strategies for peeking elements from a heap.
	 * @param heap
	 * @return element regarding to strategy.
	 */
	public T peek(Heap<T> heap);
}
