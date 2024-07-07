package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.interfaces.Bookable;

import java.time.LocalDate;
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
     * Проверяет, есть ли бронирования на указанную дату.
     *
     * @param date the date to check
     * @return true if there are bookings on the specified date, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return bookings.stream().anyMatch(booking -> booking.getBookingTime().toLocalDate().equals(date));
    }

    /**
     * Проверяет, есть ли бронирования пользователем.
     *
     * @param userName the user's name
     * @return true if the user has bookings, false otherwise
     */
    public boolean hasBookingByUser(String userName) {
        return bookings.stream().anyMatch(booking -> booking.getBookedBy().equals(userName));
    }

    /**
     * Проверяет, доступно ли рабочее место для бронирования.
     *
     * @return true if the workspace is available, false otherwise
     */
    public boolean isAvailable() {
        return bookings.isEmpty();
    }

    /**
     * Возвращаем время окончания забронированного рабочего места
     *
     * @return the booking end time as string
     */
    public String getBookingEndTime() {
        return bookings.stream()
                .map(Booking::getFormattedEndTime)
                .findFirst()
                .orElse(null);
    }
}
