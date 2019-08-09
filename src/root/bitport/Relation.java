package root.bitport;

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

@SuppressWarnings("serial")
class Relation extends Heartbeat {
	/**
	 * Relation packet
	 */
	public List<File> sharedFiles;

	Relation(Heartbeat heartbeat, List<File> sharedFiles) throws UnknownHostException {
		super(heartbeat.by.spice, heartbeat.by.designer);
		this.sharedFiles = sharedFiles;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Relation)
		{
			Relation other = (Relation) arg0;
			boolean heartbeatFlag = false, sharedFileFlag = false;
			try
			{
				heartbeatFlag = this.by.equals(other.by) && this.address.equals(other.address);
				sharedFileFlag = this.sharedFiles.equals(other.sharedFiles);
				return heartbeatFlag && sharedFileFlag;
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				sharedFileFlag = this.sharedFiles == null && other.sharedFiles == null;
				return heartbeatFlag && sharedFileFlag;
			}
		} else
			return false;
	}

}