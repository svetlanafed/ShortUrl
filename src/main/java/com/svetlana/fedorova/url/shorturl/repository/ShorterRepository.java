package com.svetlana.fedorova.url.shorturl.repository;

import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import org.springframework.data.repository.CrudRepository;

public interface ShorterRepository extends CrudRepository<Shorter, Long> {

    Shorter findByHash(String hash);

    Shorter findByOriginalUrl(String originalUrl);
}
