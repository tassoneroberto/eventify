package org.eventify.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "events")
@XmlRootElement
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 32, name = "id", updatable = false, nullable = false)
	private String id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String title;

	@Column
	private String description;

	@Column
	private Date opening;

	@Column
	private Date ending;

	@Column
	private String location;

	@Column
	private Double latitude;

	@Column
	private Double longitude;

	@Column
	private String category;

	@Column
	private Integer total_posts;

	@Column
	private Integer remaining_posts;

	@Column
	private Double price;
	
	@Column
	private Boolean deleted;

	@Column(length = 32, name = "owner_id", nullable = false)
	private String owner_id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "owner_id", insertable = false, updatable = false)
	private OrganizerInfo organizer;

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public OrganizerInfo getOrganizer() {
		return organizer;
	}

	public void setOrganizer(OrganizerInfo organizer) {
		this.organizer = organizer;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
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