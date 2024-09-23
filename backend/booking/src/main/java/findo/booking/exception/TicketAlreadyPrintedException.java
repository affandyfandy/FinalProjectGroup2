package findo.booking.exception;

public class TicketAlreadyPrintedException extends RuntimeException {
    public TicketAlreadyPrintedException(String message) {
        super(message);
    }
}