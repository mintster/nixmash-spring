package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.Hobby;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/22/15
 * Time: 11:27 AM
 */
public class HobbyDTO {

    private Long hobbyId;

    @NotEmpty
    @Length(max = Hobby.MAX_LENGTH_HOBBY_TITLE)
    private String hobbyTitle;

    private Boolean isUserHobby = false;

    public HobbyDTO() {}

    public HobbyDTO(String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

    public HobbyDTO(Long hobbyId, String hobbyTitle) {
        this.hobbyId = hobbyId;
        this.hobbyTitle = hobbyTitle;
    }

    public HobbyDTO(Hobby hobby) {
        this.hobbyId = hobby.getHobbyId();
        this.hobbyTitle = hobby.getHobbyTitle();
    }

    public Long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(Long hobbyId) {
        this.hobbyId = hobbyId;
    }

    public String getHobbyTitle() {
        return hobbyTitle;
    }

    public void setHobbyTitle(String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

    public Boolean getIsUserHobby() {
        return isUserHobby;
    }

    public void setIsUserHobby(Boolean isUserHobby) {
        this.isUserHobby = isUserHobby;
    }

    @Override
    public String toString() {
        return "HobbyDTO{" +
                "hobbyId=" + hobbyId +
                ", hobbyTitle='" + hobbyTitle + '\'' +
                ", isUserHobby=" + isUserHobby +
                '}';
    }
}
