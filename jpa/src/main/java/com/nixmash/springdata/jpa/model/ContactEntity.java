package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/29/15
 * Time: 1:24 PM
 */
@Entity
@Table(name = "contact")
@NamedQueries({
        @NamedQuery(name = "ContactEntity.findById",
                query = "select distinct c from ContactEntity c left join fetch c.contactTelDetailEntities t left join fetch c.hobbyEntities h where c.id= :id"),
        @NamedQuery(name = "ContactEntity.findAllWithDetail",
                query = "select distinct c from ContactEntity c left join fetch c.contactTelDetailEntities t left join fetch c.hobbyEntities h"),
        @NamedQuery(
                name = "ContactEntity.findContactsByEmail",
                query = "select distinct c from ContactEntity c where email like :email")
})
@SqlResultSetMapping(
        name="contactEntityResult",
        entities=@EntityResult(entityClass=ContactEntity.class)
)
public class ContactEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private int version;
    private Set<ContactTelDetailEntity> contactTelDetailEntities;
    private Set<HobbyEntity> hobbyEntities;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = false, insertable = true, updatable = true, length = 60)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, insertable = true, updatable = true, length = 40)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "birth_date", nullable = true, insertable = true, updatable = true)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Version
    @Column(name = "version", nullable = false, insertable = true, updatable = true)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ContactEntity that = (ContactEntity) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
//        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
//        if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null) return false;
//        if (email != null ? !email.equals(that.email) : that.email != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        Long result = id;
//        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
//        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
//        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
//        result = 31 * result + (email != null ? email.hashCode() : 0);
//        result = 31 * result + version;
//        return result;
//    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactEntity")
    public Set<ContactTelDetailEntity> getContactTelDetailEntities() {
        return contactTelDetailEntities;
    }

    public void setContactTelDetailEntities(Set<ContactTelDetailEntity> contactTelDetailEntities) {
        this.contactTelDetailEntities = contactTelDetailEntities;
    }

    public void addContactTelDetailEntity(ContactTelDetailEntity contactTelDetail) {
        contactTelDetail.setContactEntity(this);
        getContactTelDetailEntities().add(contactTelDetail);
    }

    @ManyToMany
    @JoinTable(name = "contact_hobby_detail", joinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "hobby_id", referencedColumnName = "hobby_id", nullable = false))
    public Set<HobbyEntity> getHobbyEntities() {
        return hobbyEntities;
    }

    public void setHobbyEntities(Set<HobbyEntity> hobbyEntities) {
        this.hobbyEntities = hobbyEntities;
    }

    public String toString() {
        return "Contact - Id: " + id + ", First name: " + firstName
                + ", Last name: " + lastName +
                ", Email: " + email +
                ", Birthday: " + birthDate;
    }

}
