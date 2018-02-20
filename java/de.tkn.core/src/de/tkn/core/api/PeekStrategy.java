package de.tkn.core.api;

@FunctionalInterface
public interface PeekStrategy<T extends Comparable<T>> {

	public T peek(Heap<T> heap);
}
