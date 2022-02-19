package com.svetlana.fedorova.url.shorturl.model.entity;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shorter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shorter_id_seq")
    @SequenceGenerator(name = "shorter_id_seq", sequenceName = "shorter_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "hash", unique = true, nullable = false)
    private String hash;

    @Column(name = "original_url", unique = true, nullable = false)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
}
