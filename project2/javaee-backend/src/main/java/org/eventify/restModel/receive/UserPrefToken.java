package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserPrefToken extends Token implements Serializable {

	public UserPrefToken(String token, String loginId) {
		super(token, loginId);
	}

	public UserPrefToken() {

	}

	private static final long serialVersionUID = 1L;

	private Double latitude;
	private Double longitude;
	private Integer rangeTime;
	private Integer rangeDistance;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getRangeTime() {
		return rangeTime;
	}

	public void setRangeTime(Integer rangeTime) {
		this.rangeTime = rangeTime;
	}

	public Integer getRangeDistance() {
		return rangeDistance;
	}

	public void setRangeDistance(Integer rangeDistance) {
		this.rangeDistance = rangeDistance;
	}

}