package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by MSI on 2016-09-25.
 */
@Entity
@Table(name = "Users")
public class Users implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @ManyToOne
    @JoinColumn(name = "profileID")
    Profiles profiles;
    @Column(name = "deleted")
    private Byte deleted;
    @Column(name = "date_creation")
    private Timestamp dateCreation;
    @Column(name = "date_updated")
    private Timestamp dateUpdated;
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;
    @Column(name = "userImage")
    private String userImage;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Timestamp getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Timestamp dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public void setProfiles(Profiles profiles) {
        this.profiles = profiles;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Users that = (Users) o;
//
//        if (id != that.id) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (password != null ? !password.equals(that.password) : that.password != null) return false;
//        if (email != null ? !email.equals(that.email) : that.email != null) return false;
//        if (deleted != null ? !deleted.equals(that.deleted) : that.deleted != null) return false;
//        if (dateCreation != null ? !dateCreation.equals(that.dateCreation) : that.dateCreation != null) return false;
//        if (dateUpdated != null ? !dateUpdated.equals(that.dateUpdated) : that.dateUpdated != null) return false;
//        if (dateDeleted != null ? !dateDeleted.equals(that.dateDeleted) : that.dateDeleted != null) return false;
//        if (userImage != null ? !userImage.equals(that.userImage) : that.userImage != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id;
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (password != null ? password.hashCode() : 0);
//        result = 31 * result + (email != null ? email.hashCode() : 0);
//        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
//        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
//        result = 31 * result + (dateUpdated != null ? dateUpdated.hashCode() : 0);
//        result = 31 * result + (dateDeleted != null ? dateDeleted.hashCode() : 0);
//        result = 31 * result + (userImage != null ? userImage.hashCode() : 0);
//        return result;
//    }
}