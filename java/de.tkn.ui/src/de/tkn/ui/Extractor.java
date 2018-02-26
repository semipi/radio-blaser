package de.tkn.ui;

@FunctionalInterface
public interface Extractor<T> {

	/**
	 * Is a column sensitive toString.
	 * @param t
	 * @param column
	 * @return
	 */
	public String extract(T t, int column);
}
