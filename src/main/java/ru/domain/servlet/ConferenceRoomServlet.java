package ru.domain.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.domain.dao.impl.ConferenceRoomDAOImpl;
import ru.domain.dao.impl.WorkspaceDAOImpl;
import ru.domain.dto.ConferenceRoomDTO;
import ru.domain.entities.ConferenceRoom;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.mapper.ConferenceRoomMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервлет для обработки запросов с конференц-залами.
 */
@WebServlet("/conference_rooms/*")
public class ConferenceRoomServlet extends HttpServlet {
    private ConferenceRoomManager conferenceRoomManager;
    private ObjectMapper objectMapper;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        this.conferenceRoomManager = new ConferenceRoomManager(new ConferenceRoomDAOImpl(), new WorkspaceDAOImpl());
        this.objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * GET-запрос для получения конференц-залов.
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
            List<ConferenceRoom> conferenceRoomList = conferenceRoomManager.findAllConferenceRooms();
            List<ConferenceRoomDTO> conferenceRoomDTOList = ConferenceRoomMapper.INSTANCE.toDTOList(conferenceRoomList);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(conferenceRoomDTOList));
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(splits[1]);
            Optional<ConferenceRoom> conferenceRoom = conferenceRoomManager.findConferenceRoomById(id);
            if (conferenceRoom.isPresent()) {
                ConferenceRoomDTO conferenceRoomDTO = ConferenceRoomMapper.INSTANCE.toDTO(conferenceRoom.get());
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(conferenceRoomDTO));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    /**
     * POST-запрос для создания нового конференц-зала.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConferenceRoomDTO conferenceRoomDTO = objectMapper.readValue(req.getReader(), ConferenceRoomDTO.class);
        Set<ConstraintViolation<ConferenceRoomDTO>> violations = validator.validate(conferenceRoomDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            ConferenceRoom conferenceRoom = ConferenceRoomMapper.INSTANCE.toEntity(conferenceRoomDTO);
            conferenceRoomManager.addConferenceRoom(conferenceRoom);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * PUT-запрос для обновления существующего конференц-зала.
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

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(splits[1]);
        ConferenceRoomDTO conferenceRoomDTO = objectMapper.readValue(req.getReader(), ConferenceRoomDTO.class);
        Set<ConstraintViolation<ConferenceRoomDTO>> violations = validator.validate(conferenceRoomDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            ConferenceRoom conferenceRoom = ConferenceRoomMapper.INSTANCE.toEntity(conferenceRoomDTO);
            conferenceRoom.setId(id);
            conferenceRoomManager.updateConferenceRoom(conferenceRoom);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * DELETE-запрос для удаления существующего конференц-зала.
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

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(splits[1]);

        try {
            conferenceRoomManager.deleteConferenceRoom(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }
}
