package at.ac.tuwien.hci.ghost.observer;


public interface Observer<T> {
	void notify(T param);
}
