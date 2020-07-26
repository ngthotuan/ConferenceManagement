package DTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
public class Conference implements DTO {
    private int id;
    private Integer placeId;
    private String name;
    private String shortDescription;
    private String detailDescription;
    private String image;
    private Date holdTime;
    private Integer conferenceTime;
    private Integer currentPerson;
    private Integer limitPerson;
    private Boolean isAcceptedRegister;
    private Place placeByPlaceId;
    private Collection<MeetingAccount> meetingAccountsById;

    public Conference() {
        this.currentPerson = 0;
        this.isAcceptedRegister = true;
    }

    public Conference(String name, Integer currentPerson) {
        this.name = name;
        this.currentPerson = currentPerson;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "placeId", nullable = true)
    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "shortDescription", nullable = true, length = 50)
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Basic
    @Column(name = "detailDescription", nullable = true, length = 200)
    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    @Basic
    @Column(name = "image", nullable = true, length = 200)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "holdTime", nullable = true)
    public Date getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(Date holdTime) {
        this.holdTime = holdTime;
    }

    @Basic
    @Column(name = "conferenceTime", nullable = true)
    public Integer getConferenceTime() {
        return conferenceTime;
    }

    public void setConferenceTime(Integer conferenceTime) {
        this.conferenceTime = conferenceTime;
    }

    @Basic
    @Column(name = "currentPerson", nullable = true)
    public Integer getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Integer currentPerson) {
        this.currentPerson = currentPerson;
    }

    @Basic
    @Column(name = "limitPerson", nullable = true)
    public Integer getLimitPerson() {
        return limitPerson;
    }

    public void setLimitPerson(Integer limitPerson) {
        this.limitPerson = limitPerson;
    }

    @Basic
    @Column(name = "isAcceptedRegister", nullable = true)
    public Boolean getAcceptedRegister() {
        return isAcceptedRegister;
    }

    public void setAcceptedRegister(Boolean acceptedRegister) {
        isAcceptedRegister = acceptedRegister;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return id == that.id &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(shortDescription, that.shortDescription) &&
                Objects.equals(detailDescription, that.detailDescription) &&
                Objects.equals(image, that.image) &&
                Objects.equals(holdTime, that.holdTime) &&
                Objects.equals(conferenceTime, that.conferenceTime) &&
                Objects.equals(currentPerson, that.currentPerson) &&
                Objects.equals(limitPerson, that.limitPerson) &&
                Objects.equals(isAcceptedRegister, that.isAcceptedRegister);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeId, name, shortDescription, detailDescription, image, holdTime, conferenceTime, currentPerson, limitPerson, isAcceptedRegister);
    }

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "placeId", referencedColumnName = "id", insertable = false, updatable = false))
    public Place getPlaceByPlaceId() {
        return placeByPlaceId;
    }

    public void setPlaceByPlaceId(Place placeByPlaceId) {
        this.placeByPlaceId = placeByPlaceId;
    }

    @OneToMany(mappedBy = "conferenceByConferenceId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Collection<MeetingAccount> getMeetingAccountsById() {
        return meetingAccountsById;
    }

    public void setMeetingAccountsById(Collection<MeetingAccount> meetingAccountsById) {
        this.meetingAccountsById = meetingAccountsById;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", placeId=" + placeId +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", detailDescription='" + detailDescription + '\'' +
                ", image='" + image + '\'' +
                ", holdTime=" + holdTime +
                ", conferenceTime='" + conferenceTime + '\'' +
                ", currentPerson=" + currentPerson +
                '}';
    }
}