package com.services.gateway.responses;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="monitorresponse")
public class MonitorResponse {
    @XmlElement(name="statusCode")
    private int statusCode;
    @XmlElement(name="status")
    private String status;
    @XmlElement(name="checkTime")
    private long checkTime;
    
    public MonitorResponse(int statusCode, String status, long checkTime) {
        this.statusCode = statusCode;
        this.status = status;
        this.checkTime = checkTime;
    }

    public long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

}
