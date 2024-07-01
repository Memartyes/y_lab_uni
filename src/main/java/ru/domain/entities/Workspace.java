package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.interfaces.Bookable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Клас представляет рабочие места.
 */
@Getter
@Setter
public class Workspace implements Bookable {
    private int id;
    private String name;
    private List<Booking> bookings;

    private boolean booked; //TODO: delete after database refactoring.
    private String bookedBy; //TODO: delete after database refactoring.
    private LocalDateTime bookingTime; //TODO: delete after database refactoring.

    /**
     * Конструктор для создания нового рабочего места.
     * @param name the workspace name;
     */
    public Workspace(String name) {
        this.name = name;
        this.bookings = new ArrayList<>();
    }

    public Workspace() {}

    /**
     * Бронируем рабочее место
     *
     * @param userName the user's name who books the workspace
     * @param bookingTime the booking time
     */
    @Override
    public void book(String userName, LocalDateTime bookingTime, int bookingDurationHours) {
        Booking booking = new Booking(userName, bookingTime, bookingDurationHours);
        this.bookings.add(booking);
    }

    /**
     * Отменяем бронирование рабочих мест
     */
    @Override
    public void cancelBooking() {
        bookings.clear();
    }

    /**
     * Удаляем бронирование по времени.
     *
     * @param bookingTime the booking time to remove the booking
     */
    @Override
    public void removeBooking(LocalDateTime bookingTime) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.getBookingTime().equals(bookingTime)) {
                iterator.remove();
                return;
            }
        }
        throw new IllegalArgumentException("Booking with the specified time not found");
    }

    /**
     * Проверяем, забронировано ли рабочее место.
     *
     * @return true if the workspace is booked, false otherwise
     */
    @Override
    public boolean isBooked() {
        return !bookings.isEmpty();
    }

    /**
     * Возвращаем время окончания забронированного рабочего места
     *
     * @return the booking end time as string
     */
        public String getBookingEndTime() {
        return bookings.stream()
                .filter(booking -> booking.getBookingTime().equals(bookingTime))
                .map(Booking::getFormattedEndTime)
                .findFirst()
                .orElse(null);
    }
}
