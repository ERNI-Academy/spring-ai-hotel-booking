package com.erni.springaihotel.controller;

import com.erni.springaihotel.model.BookingDetails;
import com.erni.springaihotel.service.BookingService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
public class DataController
{

  private final BookingService bookingService;

  public DataController(BookingService bookingService)
  {
    this.bookingService = bookingService;
  }

  @GetMapping
  public List<BookingDetails> getAllBookings()
  {
    return bookingService.getBookings();
  }
}
