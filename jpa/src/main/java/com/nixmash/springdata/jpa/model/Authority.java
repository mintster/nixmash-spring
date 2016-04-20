package com.nixmash.springdata.jpa.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    public static final int MAX_LENGTH_AUTHORITY = 30;
    public static final int MIN_LENGTH_AUTHORITY = 4;

    @Column
    @NotEmpty
    @Length(min = Authority.MIN_LENGTH_AUTHORITY, max = Authority.MAX_LENGTH_AUTHORITY)
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    private static final long serialVersionUID = 4209846601587744947L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    protected Long id;

    @Column(name = "is_locked")
    private boolean isLocked = false;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
        return result;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Authority other = (Authority) obj;
        if (authority == null) {
            if (other.authority != null)
                return false;
        } else if (!authority.equals(other.authority))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return authority;
    }

//    @Override
//    public String toString() {
//        return "Authority{" +
//                "authority='" + authority + '\'' +
//                ", id=" + id +
//                ", isLocked=" + isLocked +
//                '}';
//    }

}
