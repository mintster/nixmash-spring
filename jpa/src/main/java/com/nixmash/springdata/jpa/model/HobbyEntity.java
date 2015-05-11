package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/29/15
 * Time: 1:24 PM
 */
@Entity
@Table(name = "hobby")
public class HobbyEntity {
    private Set<ContactEntity> contactEntities;

    @Id
    @Column(name = "hobby_id")
    public String getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(String hobby_id) {
        this.hobbyId = hobby_id;
    }

    public String toString() {
        return "Hobby: " + getHobbyId();
    }

//    @ManyToMany
//    @JoinTable(name = "contact_hobby_detail",
//            joinColumns = @JoinColumn(name = "HOBBY_ID"),
//            inverseJoinColumns = @JoinColumn(name = "CONTACT_ID"))

    @ManyToMany(mappedBy = "hobbyEntities")
    public Set<ContactEntity> getContactEntities() {
        return contactEntities;
    }

    public void setContactEntities(Set<ContactEntity> contactEntities) {
        this.contactEntities = contactEntities;
    }

    private String hobbyId;


}
