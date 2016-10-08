package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MSI on 2016-09-25.
 */
@Entity
@Table(name = "AccountType")
public class AccountType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @ManyToOne
    @JoinColumn(name="profileID")
    Profiles profiles;
    @ManyToOne
    @JoinColumn(name="permissionID")
    Permissions permissions;

    public Profiles getProfiles() {
        return profiles;
    }

    public void setProfiles(Profiles profiles) {
        this.profiles = profiles;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        AccountType that = (AccountType) o;
//
//        if (id != that.id) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return id;
//    }
}
