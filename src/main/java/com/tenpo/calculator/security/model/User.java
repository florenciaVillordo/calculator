package com.tenpo.calculator.security.model;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private OffsetDateTime createDate;

    private OffsetDateTime updateDate;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    @PrePersist
    protected void onCreate() {
        this.createDate = OffsetDateTime.now();
        this.updateDate = this.createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = OffsetDateTime.now();
    }
}
