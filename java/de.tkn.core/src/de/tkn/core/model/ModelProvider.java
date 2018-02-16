package de.tkn.core.model;

import java.util.HashSet;
import java.util.Set;

public class ModelProvider<T extends Comparable<T>> {

	private Set<T> values = new HashSet<>(); 
	
	
	public ModelProvider<T> add(T t) {
		values.add(t);
		return this;
	}
	
	public ModelProvider<T> clear() {
		values.clear();
		return this;
	}
}
