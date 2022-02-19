package com.svetlana.fedorova.url.shorturl.service;

import com.svetlana.fedorova.url.shorturl.model.LongUrl;
import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import java.net.URISyntaxException;
import org.springframework.http.HttpHeaders;

public interface LinkService {

    Shorter getShortUrl(LongUrl longUrl);

    LongUrl getLongLink(String hash);

    HttpHeaders getHeaders(Shorter shorter) throws URISyntaxException;

    boolean checkShortLinkValidity(Shorter shorter);

    Shorter createNewHash(LongUrl longUrl);
}