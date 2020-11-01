package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EntityNotFoundException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 4297948102627132617L;

    private Long id;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

}
