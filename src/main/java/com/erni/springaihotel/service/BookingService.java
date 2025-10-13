package com.erni.springaihotel.service;

import com.erni.springaihotel.model.Booking;
import com.erni.springaihotel.model.BookingDetails;
import com.erni.springaihotel.model.BookingStatus;
import com.erni.springaihotel.model.SpringAIHotelDB;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BookingService
{

  private final SpringAIHotelDB springAIHotelDB;

  public BookingService(SpringAIHotelDB springAIHotelDB)
  {
    this.springAIHotelDB = springAIHotelDB;
  }

  public List<BookingDetails> getBookings()
  {
    return springAIHotelDB.getBookings().stream().map(BookingDetails::new).toList();
  }

  private Booking findBooking(String bookingNumber, String firstName, String lastName)
  {
    return springAIHotelDB.getBookings()
        .stream()
        .filter(b -> b.getBookingNumber().equalsIgnoreCase(bookingNumber))
        .filter(b -> b.getGuest().firstName().equalsIgnoreCase(firstName))
        .filter(b -> b.getGuest().lastName().equalsIgnoreCase(lastName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
  }

  public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName)
  {
    var booking = findBooking(bookingNumber, firstName, lastName);
    return toBookingDetails(booking);
  }

  public void changeBooking(String bookingNumber, String firstName, String lastName, String newDateFrom, String newDateTo)
  {
    var booking = findBooking(bookingNumber, firstName, lastName);
    if (booking.getFrom().isBefore(LocalDate.now().plusDays(1)))
    {
      throw new IllegalArgumentException("Booking cannot be changed within 24 hours of the start date.");
    }
    booking.setFrom(LocalDate.parse(newDateFrom));
    booking.setTo(LocalDate.parse(newDateTo));
  }

  public void cancelBooking(String bookingNumber, String firstName, String lastName)
  {
    var booking = findBooking(bookingNumber, firstName, lastName);
    if (booking.getFrom().isBefore(LocalDate.now().plusDays(2)))
    {
      throw new IllegalArgumentException("Booking cannot be cancelled within 48 hours of the start date.");
    }
    booking.setBookingStatus(BookingStatus.CANCELLED);
  }

  public BookingDetails toBookingDetails(Booking booking)
  {
    return new BookingDetails(booking);
  }

}
