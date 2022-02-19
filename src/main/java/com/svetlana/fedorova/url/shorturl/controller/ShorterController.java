package com.svetlana.fedorova.url.shorturl.controller;

import com.svetlana.fedorova.url.shorturl.exception_handling.IncorrectData;
import com.svetlana.fedorova.url.shorturl.exception_handling.exception.LinkExpiredException;
import com.svetlana.fedorova.url.shorturl.exception_handling.exception.LinkNotFoundException;
import com.svetlana.fedorova.url.shorturl.model.LongUrl;
import com.svetlana.fedorova.url.shorturl.model.entity.Shorter;
import com.svetlana.fedorova.url.shorturl.service.LinkService;
import com.svetlana.fedorova.url.shorturl.service.ShorterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shorter")
public class ShorterController {

    private final ShorterService service;
    private final LinkService linkService;

    @PostMapping(path = "/shorten")
    @Operation(summary = "Create short URL",
            responses = {@ApiResponse(
                    description = "Get short URL success",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shorter.class))
            ), @ApiResponse(
                    description = "Long URL is null",
                    responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinkNotFoundException.class))
            )})
    public Shorter createShortUrl(@RequestBody LongUrl longUrl) {

        return linkService.getShortUrl(longUrl);
    }

    @GetMapping(path = "/decode/{hash}")
    @Operation(summary = "Get long URL from short URL",
            responses = {@ApiResponse(
                    description = "Get long URL success",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LongUrl.class))
            ), @ApiResponse(
                    description = "Shorter doesn't exist in database",
                    responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinkNotFoundException.class))
            )})
    public LongUrl getLongLinkFromHash(@PathVariable("hash") String hash) {

        return linkService.getLongLink(hash);
    }

    @GetMapping(path = "/{hash}")
    @Operation(summary = "Make redirect to the page by short URL",
            responses = {@ApiResponse(
                    description = "Redirect to the page by long URL",
                    responseCode = "200",
                    content = @Content(mediaType = "text/html")
            ), @ApiResponse(
                    description = "Short link is expired",
                    responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinkExpiredException.class))
            ), @ApiResponse(
                    description = "Short link doesn't exist in database",
                    responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinkNotFoundException.class))
            )})
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
