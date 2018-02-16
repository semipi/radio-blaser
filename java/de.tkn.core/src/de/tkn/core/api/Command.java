package de.tkn.core.api;

public interface Command<T> {

	public void exec(T t);
}
