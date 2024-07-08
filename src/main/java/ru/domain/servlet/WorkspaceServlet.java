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
import ru.domain.dto.WorkspaceDTO;
import ru.domain.entities.Workspace;
import ru.domain.managers.WorkspaceManager;
import ru.domain.mapper.WorkspaceMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервлет для обработки запросов с рабочими местами.
 */
@WebServlet("/workspaces/*")
public class WorkspaceServlet extends HttpServlet {
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
     * GET-запрос для получения рабочего места.
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
            List<Workspace> workspaces = workspaceManager.findAllWorkspaces();
            List<WorkspaceDTO> workspaceDTOS = WorkspaceMapper.INSTANCE.toDTOList(workspaces);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(workspaceDTOS));
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(splits[1]);
            Optional<Workspace> workspace = workspaceManager.findWorkspaceById(id);
            if (workspace.isPresent()) {
                WorkspaceDTO workspaceDTO = WorkspaceMapper.INSTANCE.toDTO(workspace.get());
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(workspaceDTO));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    /**
     * POST-запрос для создания нового рабочего места.
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WorkspaceDTO workspaceDTO = objectMapper.readValue(req.getReader(), WorkspaceDTO.class);
        Set<ConstraintViolation<WorkspaceDTO>> violations = validator.validate(workspaceDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            Workspace workspace = WorkspaceMapper.INSTANCE.toEntity(workspaceDTO);
            workspaceManager.addWorkspace(workspace);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * PUT-запрос для обновления существующего рабочего места.
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
        WorkspaceDTO workspaceDTO = objectMapper.readValue(req.getReader(), WorkspaceDTO.class);
        Set<ConstraintViolation<WorkspaceDTO>> violations = validator.validate(workspaceDTO);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(violations));
            return;
        }

        try {
            Workspace workspace = WorkspaceMapper.INSTANCE.toEntity(workspaceDTO);
            workspace.setId(id);
            workspaceManager.updateWorkspace(workspace);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * DELETE-запрос для удаления существующего рабочего места.
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
            workspaceManager.deleteWorkspace(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }
}
