package de.tkn.core.api;

public interface Table<T> {

	public T get(Integer id, int signal);
	
	public T lock(T t);
	
	public boolean isLocked(T t);
	
	public void clear();
}
