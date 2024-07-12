package ru.domain;

import ru.domain.dao.BookingDAO;
import ru.domain.dao.ConferenceRoomDAO;
import ru.domain.dao.UserDAO;
import ru.domain.dao.WorkspaceDAO;
import ru.domain.dao.impl.BookingDAOImpl;
import ru.domain.dao.impl.ConferenceRoomDAOImpl;
import ru.domain.dao.impl.UserDAOImpl;
import ru.domain.dao.impl.WorkspaceDAOImpl;
import ru.domain.handlers.WorkspaceHandler;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;
import ru.domain.managers.WorkspaceManager;
import ru.domain.handlers.UserHandler;
import ru.domain.handlers.ConferenceRoomHandler;

/**
 * Main класс для запуска приложения
 */
public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        BookingDAO bookingDAO = new BookingDAOImpl();
        WorkspaceDAO workspaceDAO = new WorkspaceDAOImpl();
        ConferenceRoomDAO conferenceRoomDAO = new ConferenceRoomDAOImpl();

        UserRegistrationManager registrationManager = new UserRegistrationManager(userDAO); // Менеджер для регистрации пользователей
        UserAuthenticationManager userAuthenticationManager = new UserAuthenticationManager(userDAO); // Менеджер для аутентификации пользователей
        ConferenceRoomManager conferenceRoomManager = new ConferenceRoomManager(conferenceRoomDAO, workspaceDAO); // Менеджер для управления конференц-залами
        WorkspaceManager workspaceManager = new WorkspaceManager(workspaceDAO, bookingDAO); // Менеджер для управления рабочими местами

        UserHandler userHandle = new UserHandler(registrationManager, userAuthenticationManager); // Сервис для обработки операций с пользователями
        ConferenceRoomHandler conferenceRoomHandle = new ConferenceRoomHandler(conferenceRoomManager, workspaceManager); // Сервис для обработки операций с конференц-залами
        WorkspaceHandler workspaceHandle = new WorkspaceHandler(workspaceManager, conferenceRoomManager); // Сервис для обработки операций с рабочими местами
    }
}
