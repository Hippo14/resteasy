package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by KMacioszek on 2016-10-17.
 */
@Entity
@Table(name = "Key")
public class Key implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "user")
    Users user;
    
    @Column(name = "key")
    String key;

    public Key(Users newUser, byte[] key) {
        this.user = newUser;
        this.key = key.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
