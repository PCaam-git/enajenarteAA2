package com.svalero.enajenarte.exception;

public class DuplicateWorkshopException extends Exception {

    public DuplicateWorkshopException() {
        super("The workshop already exists");
    }
}