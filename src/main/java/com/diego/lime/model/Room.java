package com.diego.lime.model;


import jakarta.persistence.*;

@Entity
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String inviteCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private User partner;

    protected Room() {}

    public Room(String inviteCode, User creator) {
        this.inviteCode = inviteCode;
        this.creator = creator;
    }

    public boolean isFull() {
        return partner != null;
    }

    public void join(User user) {
        if(isFull()) {
            throw new IllegalStateException("Room is already full");
        }
        this.partner = user;
    }

    public boolean isMember(User user) {
        return creator.getId().equals(user.getId()) || (partner != null && partner.getId().equals(user.getId()));
    }

    public User getOtherMember (User user) {
        return creator.getId().equals(user.getId()) ? partner : creator;
    }

    public Long getId() {
        return id;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public User getCreator() {
        return creator;
    }

    public User getPartner() {
        return partner;
    }
}
