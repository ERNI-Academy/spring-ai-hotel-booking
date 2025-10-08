package com.raffaele.springaihotel.model;

import java.time.LocalDate;
import java.util.Objects;

public class Booking {
    private String bookingNumber;
    private LocalDate date;
    private Guest guest;
    private LocalDate from;
    private LocalDate to;
    private BookingStatus bookingStatus;
    private RoomType roomType;

    public Booking() {
    }

    public Booking(String bookingNumber, LocalDate date,
                   Guest guest, LocalDate from, LocalDate to, BookingStatus bookingStatus,
                   RoomType roomType) {
        this.bookingNumber = bookingNumber;
        this.date = date;
        this.guest = guest;
        this.from = from;
        this.to = to;
        this.bookingStatus = bookingStatus;
        this.roomType = roomType;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public RoomType getBookingClass() {
        return roomType;
    }

    public void setBookingClass(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingNumber, booking.bookingNumber) &&
               Objects.equals(date, booking.date) &&
               Objects.equals(guest, booking.guest) &&
               Objects.equals(from, booking.from) &&
               Objects.equals(to, booking.to) &&
               bookingStatus == booking.bookingStatus &&
               roomType == booking.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingNumber, date, guest, from, to,
                           bookingStatus, roomType);
    }

    @Override
    public String toString() {
        return "Booking{" +
               "bookingNumber='" + bookingNumber + '\'' +
               ", date=" + date +
               ", passenger=" + guest +
               ", from='" + from + '\'' +
               ", to='" + to + '\'' +
               ", bookingStatus=" + bookingStatus +
               ", roomType=" + roomType +
               '}';
    }
}
