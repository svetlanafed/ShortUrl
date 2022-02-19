package com.svetlana.fedorova.url.shorturl.service;

import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;

public interface ShorterService {

    Shorter findByHash(String hash);

    Shorter findByOriginalUrl(String originalUrl);

    Shorter save(Shorter shorter);

    void deleteById(Long id);
}
