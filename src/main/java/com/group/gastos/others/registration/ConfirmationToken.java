package com.group.gastos.others.registration;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;

@Document
@Data
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt = null;

    private String user_id;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, String user_id) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.confirmedAt = LocalDateTime.of(0,1,1,0,0,0,0);
        this.user_id = user_id;
    }
}
