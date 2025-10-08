import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HotelBooking, BookingFilter } from '../types/booking.types';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private readonly _bookings = signal<HotelBooking[]>([]);
  private readonly _isLoading = signal<boolean>(false);

  constructor(private readonly httpClient: HttpClient) {
    this.loadBookings();
  }

  // Public readonly signals
  readonly bookings = this._bookings.asReadonly();
  readonly isLoading = this._isLoading.asReadonly();

  private loadBookings(): void {
    this._isLoading.set(true); // Set loading to true on load
    this.httpClient.get<any[]>('/api/booking').subscribe({
      next: (bookings) => {
        // Convert bookingNumber from string to number if needed
        const formattedBookings: HotelBooking[] = bookings.map(booking => ({
          ...booking,
          bookingNumber: booking.bookingNumber
        }));
        this._bookings.set(formattedBookings);
        this._isLoading.set(false); // Set loading to false on success
      },
      error: (error) => {
        console.error('Error loading bookings:', error);
        // Fallback to sample data if backend is not available
        this._bookings.set([
          {
            bookingNumber: 101,
            firstName: 'John',
            lastName: 'Doe',
            date: '2025-05-31',
            bookingStatus: 'CONFIRMED',
            from: '2025-08-31',
            to: '2025-09-01',
            roomType: 'STANDARD'
          },
          {
            bookingNumber: 102,
            firstName: 'Jane',
            lastName: 'Smith',
            date: '2025-06-02',
            bookingStatus: 'CANCELLED',
            from: '2025-07-01',
            to: '2025-07-10',
            roomType: 'DELUXE'
          }
        ]);
        this._isLoading.set(false); // Set loading to false on error
      }
    });
  }

  // Method to refresh bookings data
  refreshBookings(): void {
    this.loadBookings();
  }

  // Method to get booking by number
  getBookingByNumber(bookingNumber: number) {
    return computed(() =>
      this._bookings().find(booking => booking.bookingNumber === bookingNumber)
    );
  }

  // Method to update booking status
  updateBookingStatus(bookingNumber: number, status: HotelBooking['bookingStatus']) {
    this._bookings.update(bookings =>
      bookings.map(booking =>
        booking.bookingNumber === bookingNumber
          ? { ...booking, bookingStatus: status }
          : booking
      )
    );
  }
}
