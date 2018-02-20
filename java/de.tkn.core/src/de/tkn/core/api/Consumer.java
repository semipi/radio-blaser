package de.tkn.core.api;

@FunctionalInterface
public interface Consumer<T> {

	public void add(T t);
}
