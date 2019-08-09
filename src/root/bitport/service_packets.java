package root.bitport;

import java.io.File;
import java.io.Serializable;

enum ServiceConstants implements Serializable
{
	DOWNLOAD,CHAT,CANCELED,UNAVILABLE
}
@SuppressWarnings("serial")
class ServiceRequest implements Serializable 
{
	ServiceConstants type;
	//in case type is download then file to be downloaded 
	File file;
	ServiceRequest(ServiceConstants type,File file)
	{
		this.type=type;
		this.file=file;
	}
}
@SuppressWarnings("serial")
class ServiceResponse implements Serializable 
{
	ServiceConstants type;
	Object data0;
	Object data1;
	public ServiceResponse(ServiceConstants type,Object data0,Object data1) 
	{
		this.type=type;
		this.data0=data0;
		this.data1=data1;
		
	}
}