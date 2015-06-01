package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
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
public class Hobby {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hobby hobby = (Hobby) o;

        if (hobbyId != hobby.hobbyId) return false;
        if (hobbyTitle != null ? !hobbyTitle.equals(hobby.hobbyTitle) : hobby.hobbyTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = hobbyId;
        result = 31 * result + (hobbyTitle != null ? hobbyTitle.hashCode() : 0);
        return (int)((result >> 32) ^ result);
    }

    @ManyToMany(mappedBy = "hobbies")
    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }


    public String toString() {
        return "Hobby: " + getHobbyTitle();
    }

    public void update(final String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

}
