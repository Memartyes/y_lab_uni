Приложения `Coworking Application` предназначено для работы с БД при помощи HTTP-запросов, позволяющее нам выделять Конференц-залы для Коворкинга, бронируя рабочие места для пользователей.
-

Приложение настроено и готово к запуску через класс-инициализатор `Application`, расположенный по пути:
-
`main-app/src/main/java/io/coworking/Application.java`
-

Перед запуском приложения необходимо установить нужные зависимости в локальный Maven репозиторий, воспользовавшись командой:
-
`mvn clean install`
-

URL-адреса:
-
- Users: `http://localhost:8080/y_lab_uni/users`, additional `/{id}` if you need the User by ID (`users/1` etc.).
- Bookings: `http://localhost:8080/y_lab_uni/bookings`.
- Workspaces: `http://localhost:8080/y_lab_uni/workspaces`, additional `/{id}`.
- Conference Rooms `http://localhost:8080/y_lab_uni/conference_rooms`, additional `/{id}`.

URL-адреса SpringDoc:
-
- SpringDoc Swagger-UI: `http://localhost:8080/y_lab_uni/swagger-ui/index.html`.
- API-Docs: `http://localhost:8080/y_lab_uni/v3/api-docs`.


