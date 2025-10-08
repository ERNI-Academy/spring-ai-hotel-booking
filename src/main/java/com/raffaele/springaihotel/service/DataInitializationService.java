package com.raffaele.springaihotel.service;

import com.raffaele.springaihotel.model.Booking;
import com.raffaele.springaihotel.model.RoomType;
import com.raffaele.springaihotel.model.BookingStatus;
import com.raffaele.springaihotel.model.Guest;
import com.raffaele.springaihotel.model.SpringAIHotelDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Order(2) // Run after DocumentIngestionService
public class DataInitializationService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    
    private final SpringAIHotelDB springAIHotelDB;
    private final Random random = new Random();

    public DataInitializationService(SpringAIHotelDB springAIHotelDB) {
        this.springAIHotelDB = springAIHotelDB;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting data initialization...");
        generateSampleData();
        logger.info("Data initialization completed. Generated {} guests and {} bookings",
                   springAIHotelDB.getGuests().size(), springAIHotelDB.getBookings().size());
    }

    private void generateSampleData() {
        List<Guest> guests = generateGuests();
        List<Booking> bookings = generateBookings(guests);
        
        springAIHotelDB.setGuests(guests);
        springAIHotelDB.setBookings(bookings);
    }

    private List<Guest> generateGuests() {
        List<Guest> guests = new ArrayList<>();
        
        // Generate 10 different guests
        guests.add(new Guest("John", "Doe"));
        guests.add(new Guest("Olivia", "Bennett"));
        guests.add(new Guest("Liam", "Harris"));
        guests.add(new Guest("Sophia", "Clark"));
        guests.add(new Guest("Ethan", "Lewis"));
        guests.add(new Guest("Isabella", "Walker"));
        guests.add(new Guest("Mason", "Hall"));
        guests.add(new Guest("Charlotte", "Young"));
        guests.add(new Guest("James", "King"));
        guests.add(new Guest("Ava", "Wright"));
        
        return guests;
    }

    private List<Booking> generateBookings(List<Guest> guests) {
        List<Booking> bookings = new ArrayList<>();
        
        BookingStatus[] statuses = BookingStatus.values();
        RoomType[] classes = RoomType.values();

        for (int i = 0; i < 10; i++) {
            Guest guest = guests.get(i);

            // Generate booking dates (some in the past, some in the future)
            LocalDate date = LocalDate.now().minusDays(random.nextInt(30));
            LocalDate dateFrom = LocalDate.now().plusDays(random.nextInt(30));
            LocalDate dateTo = dateFrom.plusDays((long) random.nextInt(15) + 1);
            
            // Generate booking number
            String bookingNumber = String.format("%04d", 1000 + i);
            
            // Generate seat number based on booking class
            RoomType roomType = classes[random.nextInt(classes.length)];

            // Generate booking status (mostly confirmed)
            BookingStatus status = i < 8 ? BookingStatus.CONFIRMED : statuses[random.nextInt(statuses.length)];
            
            Booking booking = new Booking(
                bookingNumber,
                date,
                guest,
                dateFrom,
                dateTo,
                status,
                roomType
            );
            
            bookings.add(booking);
        }
        
        return bookings;
    }
}
