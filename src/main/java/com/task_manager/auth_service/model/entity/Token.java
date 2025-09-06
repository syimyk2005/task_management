package com.task_manager.auth_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    @NotNull(message = "token is empty.")
    private String token;

    @Column(name = "is_logged_out")
    private boolean isRevoked;


    @Column(name = "expiry_date")
    @NotNull(message = "expire date is empty")
    private Date expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
