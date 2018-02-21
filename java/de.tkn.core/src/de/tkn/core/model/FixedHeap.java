package de.tkn.core.model;

import java.util.Iterator;

import de.tkn.core.api.Heap;

public class FixedHeap<T extends Comparable<T>> implements Heap<T> {

	private final int ll;

	private int size;

	private int count = 0;

	private final T[] heap;

	private T minValue;
	 
	private int minIndex = 0;

	@SuppressWarnings("unchecked")
	public FixedHeap(final int exponent) {
		assert exponent >= 0;

		heap = (T[]) new Comparable[size = (1 << exponent) - 1];
		ll = size & ~(size >> 1) - 1;
	}

	@Override
	public T getMax() {
		return heap[0];
	}

	@Override
	public T getMin() {
		return heap[minIndex];
	}

	@Override
	public synchronized boolean insert(final T t) {
		assert t != null;

		if (count < size) {
			heap[count] = t;

			if (minValue == null || t.compareTo(minValue) < 0) {
				minValue = t;
				minIndex = count;
			}

			heapify(t, count++);
			return true;
		}

		if (t.compareTo(minValue) < 0) {
			return false;
		}

		heap[minIndex] = t;
		heapify(t, minIndex);
		findMin();

		return true;
	}

	private void findMin() {
		final int index = ll;
		
		if(minValue == null) {
			minValue = heap[ll];
			minIndex = ll;
		}

		for (int i = index; i < size; ++i) {
			final T t = heap[i];
			
			if(t.compareTo(minValue) < 0) {
				minValue = t;
				minIndex = i;
			}
		}
	}

	private void heapify(final T t, int index) {
		// parent index
		int parent = index + 1;

		while ((parent = parent >> 1) > 0) {
			// real index
			final int real = parent - 1;

			if (t.compareTo(heap[real]) > 0) {
				swap(real, index);
				index = real;
			} else {
				return;
			}
		}
	}

	private void swap(final int i, final int j) {
		final T t = heap[i];
		heap[i] = heap[j];
		heap[j] = t;

		if (i == minIndex) {
			minIndex = j;
			return;
		}

		if (j == minIndex) {
			minIndex = i;
			return;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			int index = 0;

			@Override
			public boolean hasNext() {
				return index < count;
			}

			@Override
			public T next() {
				return heap[index++];
			}
		};
	}
}
