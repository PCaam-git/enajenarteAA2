package com.svalero.enajenarte.repository;

public class HasAssociatedRegistrationsException extends Exception {

    public HasAssociatedRegistrationsException() {
        super("The workshop has associated registrations");
    }
}