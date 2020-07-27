package DTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(MeetingAccountPK.class)
public class MeetingAccount implements DTO {
    private String userId;
    private int conferenceId;
    private Boolean isAccepted;

    private User userByUserId;
    private Conference conferenceByConferenceId;

    public MeetingAccount() {
    }

    public MeetingAccount(String userId, int conferenceId) {
        this.userId = userId;
        this.conferenceId = conferenceId;
        this.isAccepted = false;
    }

    @Id
    @Column(name = "userId", nullable = false, length = 255)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "conferenceId", nullable = false)
    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingAccount that = (MeetingAccount) o;
        return conferenceId == that.conferenceId &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(isAccepted, that.isAccepted) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, conferenceId, isAccepted);
    }

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "userId", referencedColumnName = "username", nullable = false, insertable = false, updatable = false))
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @Basic
    @Column(name = "isAccepted", nullable = true)
    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "conferenceId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false))
    public Conference getConferenceByConferenceId() {
        return conferenceByConferenceId;
    }

    public void setConferenceByConferenceId(Conference conferenceByConferenceId) {
        this.conferenceByConferenceId = conferenceByConferenceId;
    }

    @Override
    public String toString() {
        return "MeetingAccount{" +
                "userId='" + userId + '\'' +
                ", conferenceId=" + conferenceId +
                ", isAccepted=" + isAccepted +
                '}';
    }
}
