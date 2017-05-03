package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MSI on 2017-01-15.
 */
@Entity
@Table(name = "UsersLogo")
public class UsersLogo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Lob
    @Column(name = "image", length = 100000)
    byte[] image;

    public UsersLogo() {

    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
