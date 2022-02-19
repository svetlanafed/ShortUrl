package com.svetlana.fedorova.url.shorturl.service.impl;

import static java.time.ZoneOffset.UTC;

import com.svetlana.fedorova.url.shorturl.exception_handling.exception.LinkExpiredException;
import com.svetlana.fedorova.url.shorturl.exception_handling.exception.LinkNotFoundException;
import com.svetlana.fedorova.url.shorturl.model.LongUrl;
import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import com.svetlana.fedorova.url.shorturl.service.CodeGeneratorService;
import com.svetlana.fedorova.url.shorturl.service.LinkService;
import com.svetlana.fedorova.url.shorturl.service.ShorterService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {

    private static final Long VALIDITY = 10L;
    private static final int SHORT_URL_LENGTH = 5;
    private static final String NO_SHORT_LINK_IN_DB = "Short link doesn't exist in database";

    private final CodeGeneratorService codeGenerator;
    private final ShorterService service;

    @Override
    @Transactional
    public Shorter getShortUrl(LongUrl longUrl) {
        Shorter savedShorter;
        if (longUrl.getLongUrl() != null) {
            Shorter shorterFromDb = service.findByOriginalUrl(longUrl.getLongUrl());
            if (shorterFromDb != null) {
                savedShorter = shorterFromDb;
                if (checkShortLinkValidity(savedShorter)) {
                    log.info("Link {} is expired and will be deleted from database",
                            savedShorter.getHash()
                    );
                    service.deleteById(savedShorter.getId());
                    savedShorter = createNewHash(longUrl);
                }
            } else {
                savedShorter = createNewHash(longUrl);
            }
        } else {
            log.error("Long URL is empty");
            throw new LinkNotFoundException("Long URL is empty");
        }
        return savedShorter;
    }

    @Override
    public LongUrl getLongLink(String hash) {
        Shorter shorter = service.findByHash(hash);
        if (shorter != null) {
            return new LongUrl(shorter.getOriginalUrl());
        } else {
            log.error("Short link {} doesn't exist in database", hash);
            throw new LinkNotFoundException(NO_SHORT_LINK_IN_DB);
        }
    }

    @Override
    public HttpHeaders getHeaders(Shorter shorter) throws URISyntaxException {
        if (shorter != null) {
            if (checkShortLinkValidity(shorter)) {
                log.error("Link {} is expired", shorter.getHash());
                throw new LinkExpiredException("Link is expired");
            }
            URI uri = new URI(shorter.getOriginalUrl());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);
            return headers;
        } else {
            log.error(NO_SHORT_LINK_IN_DB);
            throw new LinkNotFoundException(NO_SHORT_LINK_IN_DB);
        }
    }

    @Override
    public boolean checkShortLinkValidity(Shorter shorter) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expired = shorter.getCreatedAt().plusMinutes(VALIDITY);
        return now.isAfter(expired);
    }

    @Override
    public Shorter createNewHash(LongUrl longUrl) {
        String hash = codeGenerator.generate(SHORT_URL_LENGTH);
        Shorter shorter = new Shorter(
                null,
                hash,
                longUrl.getLongUrl(),
                Instant.now().atZone(UTC)
        );
        return service.save(shorter);
    }
}
