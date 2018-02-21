package de.tkn.core.api;

@FunctionalInterface
public interface Consumer<S, T> {

	public void add(S s, T t);
}
