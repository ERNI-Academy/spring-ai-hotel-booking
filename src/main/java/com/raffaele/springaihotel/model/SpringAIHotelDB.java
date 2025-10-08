package com.raffaele.springaihotel.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringAIHotelDB
{

  private List<Guest> guests = new ArrayList<>();
  private List<Booking> bookings = new ArrayList<>();

  public List<Guest> getGuests() {
    return guests;
  }
  public void setGuests(List<Guest> guests) {
    this.guests = guests;
  }
  public List<Booking> getBookings() {
    return bookings;
  }
  public void setBookings(List<Booking> bookings) {
    this.bookings = bookings;
  }

}
