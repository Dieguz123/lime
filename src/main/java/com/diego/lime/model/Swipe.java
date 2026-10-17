package com.diego.lime.model;


import jakarta.persistence.*;

@Entity
@Table(name = "swipes", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id", "film_id"}))
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Column(nullable = false)
    private boolean liked;

    protected Swipe() {
    }

    public Swipe(Room room, User user, Film film, boolean liked) {
        this.room = room;
        this.user = user;
        this.film = film;
        this.liked = liked;
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public Film getFilm() {
        return film;
    }

    public boolean isLiked() {
        return liked;
    }

}
