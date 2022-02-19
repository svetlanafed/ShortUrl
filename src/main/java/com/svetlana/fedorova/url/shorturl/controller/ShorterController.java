package com.svetlana.fedorova.url.shorturl.controller;

import com.svetlana.fedorova.url.shorturl.exception_handling.IncorrectData;
import com.svetlana.fedorova.url.shorturl.model.LongUrl;
import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import com.svetlana.fedorova.url.shorturl.service.LinkService;
import com.svetlana.fedorova.url.shorturl.service.ShorterService;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ShorterController {

    private final ShorterService service;
    private final LinkService linkService;

    @PostMapping(path = "/shorten")
    public Shorter createShortUrl(@RequestBody LongUrl longUrl) {

        return linkService.getShortUrl(longUrl);
    }

    @GetMapping(path = "/decode/{hash}")
    public LongUrl getLongLinkFromHash(@PathVariable("hash") String hash) {

        return linkService.getLongLink(hash);
    }

    @GetMapping(path = "/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash)
            throws URISyntaxException {
        Shorter shorter = service.findByHash(hash);
        return new ResponseEntity<>(linkService.getHeaders(shorter), HttpStatus.MOVED_PERMANENTLY);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
