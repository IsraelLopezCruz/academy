package com.priceshoes.academy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Chapter not found")
public class ChapterNotFoundException extends RuntimeException {
	@Serial
    private static final long serialVersionUID = -6933586567076286124L;

}