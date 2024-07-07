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
import ru.domain.dao.UserDAO;
import ru.domain.dao.impl.UserDAOImpl;
import ru.domain.dto.UserDTO;
import ru.domain.entities.User;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;
import ru.domain.mapper.UserMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервлет для обработки запросов с пользователями.
 */
@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    private UserRegistrationManager userRegistrationManager;
    private ObjectMapper objectMapper;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        this.userRegistrationManager = new UserRegistrationManager(new UserDAOImpl());
        this.objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * GET-запрос для получения пользователей.
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
            List<User> users = userRegistrationManager.getAllUsers();
            List<UserDTO> userDTOS = UserMapper.INSTANCE.toDTOList(users);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(userDTOS));
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(splits[1]);
            Optional<User> user = userRegistrationManager.getUserById(id);
            if (user.isPresent()) {
                UserDTO userDTO = UserMapper.INSTANCE.toDTO(user.get());
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(userDTO));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    /**
     * POST-запрос для создания нового пользователя.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            User user = UserMapper.INSTANCE.toEntity(userDTO);
            userRegistrationManager.registerUser(user.getName(), user.getEmail(), user.getPassword());
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * PUT-запрос для обновления существующего пользователя.
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
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            User user = UserMapper.INSTANCE.toEntity(userDTO);
            user.setId(id);
            userRegistrationManager.updateUser(user);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * DELETE-запрос для удаления существующего пользователя.
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
            userRegistrationManager.deleteUser(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }
}
