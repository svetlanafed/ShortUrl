package com.svetlana.fedorova.url.shorturl.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {

    private final RandomStringGenerator randomStringGenerator;

    public CodeGeneratorService() {
        this.randomStringGenerator = new RandomStringGenerator
                .Builder().filteredBy(CodeGeneratorService::isLatinLetterOrDigit)
                .build();
    }

    public String generate(int length) {
        return randomStringGenerator.generate(length);
    }

    private static boolean isLatinLetterOrDigit(int codePoint) {
        return ('a' <= codePoint && codePoint <= 'z')
                || ('A' <= codePoint && codePoint <= 'Z')
                || ('0' <= codePoint && codePoint <= '9')
                || ('+' == codePoint)
                || ('_' == codePoint)
                || ('-' == codePoint);
    }
}
