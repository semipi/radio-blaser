package de.tkn.ui;

public interface Extractor<T> {

	public String extract(T t, int column);
}
