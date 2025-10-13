package com.erni.springaihotel.service;

import com.erni.springaihotel.model.BookingDetails;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class BookingTools
{

  private final BookingService bookingService;

  public BookingTools(BookingService bookingService)
  {
    this.bookingService = bookingService;
  }

  @Tool(description = "Get booking details")
  public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName)
  {
    try
    {
      return bookingService.getBookingDetails(bookingNumber, firstName, lastName);
    }
    catch (Exception _)
    {
      return new BookingDetails(bookingNumber, firstName, lastName, null, null, null, null, null);
    }
  }

  @Tool(description = "Change booking dates")
  public void changeBooking(String bookingNumber, String firstName, String lastName, String newDateFrom, String newDateTo)
  {
    bookingService.changeBooking(bookingNumber, firstName, lastName, newDateFrom, newDateTo);
  }

  @Tool(description = "Cancel booking")
  public void cancelBooking(String bookingNumber, String firstName, String lastName)
  {
    bookingService.cancelBooking(bookingNumber, firstName, lastName);
  }
}
