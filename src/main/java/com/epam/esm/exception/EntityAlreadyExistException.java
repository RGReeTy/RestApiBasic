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
public class EntityAlreadyExistException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -3926447876672338597L;

    private Long id;

    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public EntityAlreadyExistException(String message, Long id) {
        super(message);
        this.id = id;
    }

}
