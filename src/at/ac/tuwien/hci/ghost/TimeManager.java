package at.ac.tuwien.hci.ghost;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.os.AsyncTask;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.observer.Subject;

/**
 * Timer which raises events at defined intervals, the special thing about this
 * timer is that it raises the event in context of the UI-Thread
 */
public class TimeManager extends AsyncTask<Void, Void, Void> implements Subject<TimeManager> {
	private volatile boolean destroyMe = false;

	/** Synchronization context */
	private Object syncLock = new Object();
	/** Indicates if the timer is enabled */
	private boolean enabled = false;
	/** The interval of the timer interrupts */
	private int interval = 100;
	/** Observer for the timer */
	private List<Observer<TimeManager>> observer = null;
	/** Lock combined ui condition */
	private final Lock uiLock = new ReentrantLock();
	/** signals if the ui callback has been processed */
	private final Condition uiCond = uiLock.newCondition();

	public TimeManager(Observer<TimeManager> callback) {
		this.observer = new Vector<Observer<TimeManager>>();
		this.observer.add(callback);
	}

	public boolean getEnabled() {
		synchronized (syncLock) {
			return enabled;
		}
	}

	public void setEnabled(boolean value) {
		synchronized (syncLock) {
			enabled = value;
		}
	}

	public int getInterval() {
		synchronized (syncLock) {
			return interval;
		}
	}

	public void setInterval(int value) {
		synchronized (syncLock) {
			interval = value;
		}
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		int maxThreadSleep = 10;
		long lastCall = System.nanoTime() / 1000 / 1000;

		try {
			while (!destroyMe) {
				Thread.sleep(Math.min(maxThreadSleep, interval));

				if (enabled && (System.nanoTime() / 1000 / 1000) - lastCall >= interval) {
					try {
						uiLock.lock();
						publishProgress((Void[]) null);
						uiCond.await();
						lastCall = System.nanoTime() / 1000 / 1000;
					} finally {
						uiLock.unlock();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);

		// notify all observer
		notifyAll(this);

		try {
			uiLock.lock();
			uiCond.signal();
		} finally {
			uiLock.unlock();
		}

	}

	@Override
	public void addObserver(Observer<TimeManager> o) {
		observer.add(o);

	}

	@Override
	public void notifyAll(TimeManager event) {
		for (Observer<TimeManager> o : this.observer) {
			o.notify(event);
		}

	}

	@Override
	public void removeObserver(Observer<TimeManager> o) {
		observer.remove(o);
	}

}
