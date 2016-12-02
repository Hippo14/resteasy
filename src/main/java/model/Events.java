package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by MSI on 2016-09-25.
 */
@Entity
@Table(name = "Events")
public class Events implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "longitude")
    double longitude;
    @Column(name = "latitude")
    double latitude;
    @ManyToOne
    @JoinColumn(name="userID")
    Users users;
    @ManyToOne
    @JoinColumn(name="categoryID")
    Category category;
    @Column(name = "deleted")
    Byte deleted;
    @Column(name = "active")
    Byte active;
    @Column(name = "date_creation")
    Timestamp date_creation;
    @Column(name = "date_updated", nullable = false)
    Timestamp date_updated;
    @Column(name = "date_ending")
    Timestamp date_ending;

    public Events() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

    public Timestamp getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Timestamp date_creation) {
        this.date_creation = date_creation;
    }

    public Timestamp getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Timestamp date_updated) {
        this.date_updated = date_updated;
    }

    public Timestamp getDate_ending() {
        return date_ending;
    }

    public void setDate_ending(Timestamp date_ending) {
        this.date_ending = date_ending;
    }

    public String getShortDescription() {
        StringBuilder shortDescription = new StringBuilder("");
        shortDescription.append(description.substring(0, 32)).append("...");
        return shortDescription.toString();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Events that = (Events) o;
//
//        if (id != that.id) return false;
//        if (Double.compare(that.longitude, longitude) != 0) return false;
//        if (Double.compare(that.latitude, latitude) != 0) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (description != null ? !description.equals(that.description) : that.description != null) return false;
//        if (deleted != null ? !deleted.equals(that.deleted) : that.deleted != null) return false;
//        if (active != null ? !active.equals(that.active) : that.active != null) return false;
//        if (dateCreation != null ? !dateCreation.equals(that.dateCreation) : that.dateCreation != null) return false;
//        if (dateUpdated != null ? !dateUpdated.equals(that.dateUpdated) : that.dateUpdated != null) return false;
//        if (dateEnding != null ? !dateEnding.equals(that.dateEnding) : that.dateEnding != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        result = id;
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (description != null ? description.hashCode() : 0);
//        temp = Double.doubleToLongBits(longitude);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(latitude);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
//        result = 31 * result + (active != null ? active.hashCode() : 0);
//        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
//        result = 31 * result + (dateUpdated != null ? dateUpdated.hashCode() : 0);
//        result = 31 * result + (dateEnding != null ? dateEnding.hashCode() : 0);
//        return result;
//    }
}
