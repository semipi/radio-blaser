package de.tkn.core.api;

/**
 * Guarantees unique creation of elements.
 *
 * @param <T>
 */
public interface Table<T> {

	/**
	 * Returns an unique instance of T.
	 * 
	 * @param id
	 * @param signal
	 * @return a new instance if it did not exist or the already existing one.
	 */
	public T get(Integer id, int signal);

	/**
	 * Locks an instance. The signal will then not be averaged any more.
	 * 
	 * @param t
	 * @return
	 */
	public T lock(T t);

	/**
	 * @param t
	 * @return <code>true</code> if element is locked.
	 */
	public boolean isLocked(T t);

	/**
	 * Clears the table and deletes all elements.
	 */
	public void clear();
}
