package root.pixelShare;

import java.io.Serializable;

public class Data implements Serializable {
	private static final long serialVersionUID = -1364904863356762504L;
	byte data[]; 
	long sid;

	Data(byte data[]) {
		this.data = data;
	}
}
