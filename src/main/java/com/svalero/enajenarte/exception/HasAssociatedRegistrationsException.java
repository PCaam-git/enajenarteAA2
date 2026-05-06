package com.svalero.enajenarte.exception;

public class HasAssociatedRegistrationsException extends Exception {

    public HasAssociatedRegistrationsException() {
        super("The workshop has associated registrations");
    }
}