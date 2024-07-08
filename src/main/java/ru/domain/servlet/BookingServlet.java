package ru.domain.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.domain.dao.impl.BookingDAOImpl;
import ru.domain.dao.impl.WorkspaceDAOImpl;
import ru.domain.dto.BookingDTO;
import ru.domain.entities.Booking;
import ru.domain.managers.WorkspaceManager;
import ru.domain.mapper.BookingMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервлет для обработки запросов с бронированиями.
 */
@WebServlet("/bookings/*")
public class BookingServlet extends HttpServlet {
    private WorkspaceManager workspaceManager;
    private ObjectMapper objectMapper;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        this.workspaceManager = new WorkspaceManager(new WorkspaceDAOImpl(), new BookingDAOImpl());
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * GET-запрос для получения бронирования.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Booking> bookings = workspaceManager.findAllBookings();
            List<BookingDTO> bookingDTOS = BookingMapper.INSTANCE.toDTOList(bookings);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(bookingDTOS));
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(splits[1]);
            Optional<Booking> booking = workspaceManager.findBookingById(id);
            if (booking.isPresent()) {
                BookingDTO bookingDTO = BookingMapper.INSTANCE.toDTO(booking.get());
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(bookingDTO));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    /**
     * POST-запрос для создания нового бронирования.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookingDTO bookingDTO = objectMapper.readValue(req.getReader(), BookingDTO.class);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
            workspaceManager.bookWorkspace(booking.getWorkspaceId(), booking.getBookedBy(), booking.getBookingTime(), booking.getBookingDurationHours());
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * PUT-запрос для обновления существующего бронирования.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] split = pathInfo.split("/");
        if (split.length == 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(split[1]);
        BookingDTO bookingDTO = objectMapper.readValue(req.getReader(), BookingDTO.class);
        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(bookingDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
            booking.setId(id);
            workspaceManager.updateBooking(booking);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * DELETE-запрос для удаления существующего бронирования.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] split = pathInfo.split("/");
        if (split.length == 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(split[1]);

        try {
            workspaceManager.cancelBooking(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }
}
