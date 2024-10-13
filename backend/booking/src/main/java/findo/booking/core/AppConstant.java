package findo.booking.core;

import lombok.Getter;

@Getter
public enum AppConstant {
    BookingTicketSuccessMsg("Ticket successfully printed"),
    BookingPDFErrorMsg("Error generating PDF"),
    BookingTicketPrintedMsg("Ticket has already been printed"),
    BookingNotFoundMsg("Booking not found"),
    BookingScheduleLateMsg("Schedule Movie it's Over"),
    BookingInsufficientBalanceMsg("Insufficient balance");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
