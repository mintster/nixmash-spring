package com.nixmash.springdata.hibernate.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/30/15
 * Time: 4:44 PM
 */
@Entity
@Table(name = "hobby", schema = "", catalog = "dev_hibernate")
public class HobbyEntity {
    private String hobbyId;
    private Set<ContactEntity> contactEntities;

    @Id
    @Column(name = "hobby_id", nullable = false, insertable = true, updatable = true, length = 20)
    public String getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(String hobbyId) {
        this.hobbyId = hobbyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HobbyEntity that = (HobbyEntity) o;

        if (hobbyId != null ? !hobbyId.equals(that.hobbyId) : that.hobbyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hobbyId != null ? hobbyId.hashCode() : 0;
    }

    @ManyToMany(mappedBy = "hobbyEntities")
    public Set<ContactEntity> getContactEntities() {
        return contactEntities;
    }

    public void setContactEntities(Set<ContactEntity> contactEntities) {
        this.contactEntities = contactEntities;
    }
}
