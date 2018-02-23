package de.tkn.core.api;

@FunctionalInterface
public interface Command<T> {

	public void exec(T t);
}
