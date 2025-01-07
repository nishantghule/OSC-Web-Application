package com.in.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "session_table")
public class SessionEntity {

    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "loginDevice_name")
    private String loginDevice;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

}
