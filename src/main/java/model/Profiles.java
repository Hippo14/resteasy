package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MSI on 2016-09-25.
 */
@Entity
@Table(name = "Profiles")
public class Profiles implements Serializable{
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;


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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Profiles that = (Profiles) o;
//
//        if (id != that.id) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id;
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        return result;
//    }
}
