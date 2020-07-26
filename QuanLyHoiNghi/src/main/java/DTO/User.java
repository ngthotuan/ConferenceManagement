package DTO;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class User implements DTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private Boolean isAdmin;
    private Boolean isBlocked;

    private Collection<MeetingAccount> meetingAccountsByUsername;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
        this.isBlocked = false;
    }

    public User(String[] args) {
        try {
            this.username = args[0];
            this.password = args[1];
            this.name = args[2];
            this.email = args[3];
            this.isAdmin = false;
            this.isBlocked = false;
        } catch (IndexOutOfBoundsException e){
            System.err.println("Failed to create user with array args!!!");
        }
    }

    @Id
    @Column(name = "username", nullable = false, length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    @Basic
//    @Column(name = "isAdmin", nullable = true)
//    public Byte getIsAdmin() {
//        return isAdmin;
//    }
//
//    public void setIsAdmin(Byte isAdmin) {
//        this.isAdmin = isAdmin;
//    }

    @Basic
    @Column(name = "isAdmin", nullable = true)
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Basic
    @Column(name = "isBlocked", nullable = true)
    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(isAdmin, user.isAdmin) &&
                Objects.equals(isBlocked, user.isBlocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, name, email, isAdmin, isBlocked);
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<MeetingAccount> getMeetingAccountsByUsername() {
        return meetingAccountsByUsername;
    }

    public void setMeetingAccountsByUsername(Collection<MeetingAccount> meetingAccountsByUsername) {
        this.meetingAccountsByUsername = meetingAccountsByUsername;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
