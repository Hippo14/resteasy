package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MSI on 2016-09-25.
 */
@Entity
@Table(name = "UsersEvents")
public class UsersEvents implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @ManyToOne
    @JoinColumn(name="eventID")
    Events events;
    @ManyToOne
    @JoinColumn(name="userID")
    Users users;

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        UsersEvents that = (UsersEvents) o;
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
