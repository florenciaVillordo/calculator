package com.tenpo.calculator.model;

import com.tenpo.calculator.security.model.User;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * Entity that represents the history of the transaction
 *
 * @author FloVillordo
 */

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TxHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private OffsetDateTime createDate;

    private String txType;

    @ManyToOne
    private User user;

    private TxResult result;


    @PrePersist
    protected void onCreate() {
        createDate = OffsetDateTime.now();
    }

}
