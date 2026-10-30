package com.diego.lime.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 6)
    private String inviteCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private User partner;

    @ManyToMany
    @JoinTable(
            name = "room_providers",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    private Set<Provider> providers = new HashSet<>();

    protected Room() {}

    public Room(String name, String inviteCode, User creator) {
        this.name = name;
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

    public String getName() {
        return name;
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

    public Set<Provider> getProviders() {
        return providers;
    }

    public void setProviders(Set<Provider> providers) {
        this.providers = providers;
    }
}
