package ru.domain.managers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.domain.dao.BookingDAO;
import ru.domain.dao.WorkspaceDAO;
import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;
import ru.domain.util.WorkingDateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Определяем класс для управления рабочими местами
 */
@Service
@Getter
public class WorkspaceManager {
    private final WorkspaceDAO workspaceDAO;
    private final BookingDAO bookingDAO;

    @Autowired
    public WorkspaceManager(WorkspaceDAO workspaceDAO, BookingDAO bookingDAO) {
        this.workspaceDAO = workspaceDAO;
        this.bookingDAO = bookingDAO;
    }

    /**
     * Добавляем рабочее место.
     *
     * @param workspace the workspace to add
     */
    public void addWorkspace(Workspace workspace) {
        workspaceDAO.addWorkspace(workspace);
    }

    /**
     * Находим рабочее место по его ID.
     *
     * @param id the workspace ID
     * @return the workspace
     */
    public Optional<Workspace> findWorkspaceById(int id) {
        return workspaceDAO.findWorkspaceById(id);
    }

    /**
     * Находим все рабочие места.
     *
     * @return the list of workspaces
     */
    public List<Workspace> findAllWorkspaces() {
        return workspaceDAO.findAllWorkspaces();
    }

    /**
     * Обновляем рабочее место.
     *
     * @param workspace the workspace object to update
     */
    public void updateWorkspace(Workspace workspace) {
        workspaceDAO.updateWorkspace(workspace);
    }

    /**
     * Удаляем рабочее место по его ID.
     *
     * @param id the workspace ID
     */
    public void deleteWorkspace(int id) {
        workspaceDAO.deleteWorkspace(id);
    }

    /**
     * Проверяем доступность рабочего места на определенное время.
     *
     * @param workspaceId the workspace ID
     * @param bookingTime the booking time
     * @return true if the workspace is available at the given time, false otherwise
     */
    public boolean isWorkspaceAvailable(int workspaceId, LocalDateTime bookingTime) {
        return WorkingDateTimeUtil.isWithinWorkingHours(bookingTime) && WorkingDateTimeUtil.isWorkingDay(bookingTime) && workspaceDAO.isWorkspaceAvailable(workspaceId, bookingTime);
    }

    /**
     * Бронируем рабочее место.
     *
     * @param workspaceId the workspace ID
     * @param bookedBy the user who booked the workspace
     * @param bookingTime the booking time
     * @param durationHours the booking duration in hours
     */
    public void bookWorkspace(int workspaceId, String bookedBy, LocalDateTime bookingTime, int durationHours) {
        if (isWorkspaceAvailable(workspaceId, bookingTime)) {
            Booking booking = new Booking();
            booking.setWorkspaceId(workspaceId);
            booking.setBookedBy(bookedBy);
            booking.setBookingTime(bookingTime);
            booking.setBookingDurationHours(durationHours);
            bookingDAO.addBooking(booking);
        } else {
            throw new IllegalArgumentException("Workspace is not available at the specified time");
        }
    }

    /**
     * Отменяем бронирование.
     *
     * @param bookingId the booking ID
     */
    public void cancelBooking(int bookingId) {
        bookingDAO.deleteBooking(bookingId);
    }

    /**
     * Находим бронирование по его ID.
     * @param bookingId
     * @return
     */
    public Optional<Booking> findBookingById(int bookingId) {
        return bookingDAO.findBookingById(bookingId);
    }

    /**
     * Находим бронирования по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings by workspace ID
     */
    public List<Booking> findBookingsByWorkspaceId(int workspaceId) {
        return bookingDAO.findBookingsByWorkspaceId(workspaceId);
    }

    /**
     * Находим бронирования по имени пользователя.
     *
     * @param bookedBy the user who booked the workspace
     * @return the list of bookings by user's name
     */
    public List<Booking> findBookingsByUser(String bookedBy) {
        return bookingDAO.findBookingsByUser(bookedBy);
    }

    /**
     * Находим бронирования на определенную дату.
     *
     * @param date the booking date
     * @return the list of bookings on the specified date
     */
    public List<Booking> findBookingsByDate(LocalDateTime date) {
        return bookingDAO.findBookingsByDate(date);
    }

    /**
     * Отменяем бронирования по имени пользователя.
     *
     * @param bookedBy the user who booked the workspace
     */
    public void cancelBookingsByUser(String bookedBy) {
        bookingDAO.cancelBookingsByUser(bookedBy);
    }

    /**
     * Находим все бронирования.
     *
     * @return
     */
    public List<Booking> findAllBookings() {
        return bookingDAO.findAllBookings();
    }

    /**
     * Обновляем бронирование.
     *
     * @param booking
     */
    public void updateBooking(Booking booking) {
        bookingDAO.updateBooking(booking);
    }
}
