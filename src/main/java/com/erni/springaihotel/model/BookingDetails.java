package com.erni.springaihotel.model;

import java.time.LocalDate;

public record BookingDetails(
    String bookingNumber,
    String firstName,
    String lastName,
    LocalDate date,
    BookingStatus bookingStatus,
    LocalDate from,
    LocalDate to,
    String roomType
)
{

  public BookingDetails(Booking booking)
  {
    this(
        booking.getBookingNumber(),
        booking.getGuest().firstName(),
        booking.getGuest().lastName(),
        booking.getDate(),
        booking.getBookingStatus(),
        booking.getFrom(),
        booking.getTo(),
        booking.getBookingClass().name()
    );
  }
}
