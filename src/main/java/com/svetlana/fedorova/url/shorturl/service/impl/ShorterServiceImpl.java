package com.svetlana.fedorova.url.shorturl.service.impl;

import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import com.svetlana.fedorova.url.shorturl.repository.ShorterRepository;
import com.svetlana.fedorova.url.shorturl.service.ShorterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShorterServiceImpl implements ShorterService {

    private final ShorterRepository shorterRepository;

    @Override
    public Shorter findByHash(String hash) {

        return shorterRepository.findByHash(hash);
    }

    @Override
    public Shorter findByOriginalUrl(String originalUrl) {

        return shorterRepository.findByOriginalUrl(originalUrl);
    }

    @Override
    public Shorter save(Shorter shorter) {

        return shorterRepository.save(shorter);
    }

    @Override
    public void deleteById(Long id) {
        shorterRepository.deleteById(id);
    }
}
