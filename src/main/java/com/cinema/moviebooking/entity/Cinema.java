package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cinema extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 20)
    private String contact;

    @Builder
    public Cinema(String name, String region, String address, String contact) {
        this.name = name;
        this.region = region;
        this.address = address;
        this.contact = contact;
    }

    public void updateInfo(String name, String region, String address, String contact) {
        if (name != null) this.name = name;
        if (region != null) this.region = region;
        if (address != null) this.address = address;
        if (contact != null) this.contact = contact;
    }
}
