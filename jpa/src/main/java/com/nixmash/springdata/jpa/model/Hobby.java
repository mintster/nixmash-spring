package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/11/15
 * Time: 3:25 PM
 */
@Entity
@Table(name = "hobbies")
public class Hobby implements Serializable {
    private static final long serialVersionUID = 1275629204584766338L;
    private Long hobbyId;
    private String hobbyTitle;
    private Set<Contact> contacts;

    public static final int MAX_LENGTH_HOBBY_TITLE = 20;

    public Hobby() {
    }

    public Hobby(String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "hobby_id", nullable = false, insertable = true, updatable = true)
    public Long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(Long hobbyId) {
        this.hobbyId = hobbyId;
    }

    @Basic
    @Column(name = "hobby_title", nullable = false, insertable = true, updatable = true, length = MAX_LENGTH_HOBBY_TITLE)
    public String getHobbyTitle() {
        return hobbyTitle;
    }

    public void setHobbyTitle(String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

    @ManyToMany(mappedBy = "hobbies")
    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }


    public String toString() {
        return getHobbyTitle();
    }

    public void update(final String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

}
