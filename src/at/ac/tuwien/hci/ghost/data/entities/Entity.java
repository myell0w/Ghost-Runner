package at.ac.tuwien.hci.ghost.data.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {
	/** the serial version UID */
	private static final long serialVersionUID = -5700235356938057111L;

	public abstract long getID();
	public abstract void setID(long id);
}
