package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KMacioszek on 2016-10-17.
 */
@Entity
@Table(name = "UsersKeys")
public class UsersKeys implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="userID")
    Users user;

    @Column(name = "date_expire")
    Timestamp dateExpire;

    @Column(name = "keys")
    byte[] key;

    public UsersKeys() { }

    public UsersKeys(Users newUser, Timestamp dateExpire, byte[] key) {
        this.user = newUser;
        this.dateExpire = dateExpire;
        this.key = key;
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

    public byte[] getKey() {
        return key;
    }
    public Timestamp getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Timestamp dateExpire) {
        this.dateExpire = dateExpire;
    }


    public void setKey(byte[] key) {
        this.key = key;
    }
}
