package at.ac.tuwien.hci.ghost.observer;

public interface Subject<T> {
	public void addObserver(Observer<T> o);

	public void removeObserver(Observer<T> o);
	public void removeObserver(int index);

	public void notifyAll(T event);
}
