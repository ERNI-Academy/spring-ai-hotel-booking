export interface HotelBooking {
  bookingNumber: number;
  firstName: string;
  lastName: string;
  date: string;
  bookingStatus: 'CONFIRMED' | 'CANCELLED';
  from: string;
  to: string;
  roomType: string;
}

export interface BookingFilter {
  status?: string;
  dateFrom?: string;
  dateTo?: string;
  searchTerm?: string;
}
