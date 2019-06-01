package org.eventify.restModel.send;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.entity.Event;

@XmlRootElement
public class EventDI implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private String description;

	private Date opening;

	private Date ending;

	private String location;

	private Double latitude;

	private Double longitude;

	private String phone;

	private String owner_name;

	private String category;

	private Integer total_posts;

	private Integer remaining_posts;

	private Double price;
	
	private Boolean deleted;

	public EventDI(Event entity) {
		this.category = entity.getCategory();
		this.description = entity.getDescription();
		this.ending = entity.getEnding();
		this.opening = entity.getOpening();
		this.id = entity.getId();
		this.latitude = entity.getLatitude();
		this.longitude = entity.getLongitude();
		this.location = entity.getLocation();
		this.phone = entity.getOrganizer().getPhone();
		this.owner_name = entity.getOrganizer().getUsername();
		this.price = entity.getPrice();
		this.deleted = entity.getDeleted();
		this.title = entity.getTitle();
		this.total_posts = entity.getTotal_posts();
		this.remaining_posts = entity.getRemaining_posts();
	}

	public EventDI(String id, String title, String description, Date opening, Date ending, String location,
			Double latitude, Double longitude, String phone, String owner_name, String category, Integer total_posts,
			Integer remaining_posts, Double price, Boolean deleted) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.opening = opening;
		this.ending = ending;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phone = phone;
		this.owner_name = owner_name;
		this.category = category;
		this.total_posts = total_posts;
		this.remaining_posts = remaining_posts;
		this.price = price;
		this.deleted = deleted;
	}

	public EventDI() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOpening() {
		return opening;
	}

	public void setOpening(Date opening) {
		this.opening = opening;
	}

	public Date getEnding() {
		return ending;
	}

	public void setEnding(Date ending) {
		this.ending = ending;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getTotal_posts() {
		return total_posts;
	}

	public void setTotal_posts(Integer total_posts) {
		this.total_posts = total_posts;
	}

	public Integer getRemaining_posts() {
		return remaining_posts;
	}

	public void setRemaining_posts(Integer remaining_posts) {
		this.remaining_posts = remaining_posts;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EventDI)) {
			return false;
		}
		EventDI other = (EventDI) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}