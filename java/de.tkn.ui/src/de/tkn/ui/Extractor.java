package de.tkn.ui;

@FunctionalInterface
public interface Extractor<T> {

	public String extract(T t, int column);
}
