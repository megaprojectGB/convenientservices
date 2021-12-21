# convenientservices
# Описание
## Для чего всё это
Проект является платформой поиска поставщиков услуг с одной стороны, и клиентов – с другой.
В проекте реализуется взаимодействие трёх видов пользователей
* Пользователь услуг
* Пользователь, предоставляющий услуги - мастер
* Пользователь, владелец бизнеса и точек предоставления услуг, нанимающий мастеров

## Основной функционал
Каждый вид пользователя регистрируется в системе, и последующее взаимодействие происходит уже внутри системы. Регистрация выполняется с подтверждением отправкой на почту пользователся ссылки, содержащей код активации. Если код активации не будет введён в течение 48 часов после регистрации - пользователь блокируется.
Для каждого вида пользователей предусмотрен свой набор возможных действий.
- Пользователь услуг имеет возможность бронировать услуги - выбирать точку предоставления услуги, мастера, свободное время. При необходимости, пользователь может сформировать свой список приоритетных точек предоставления услуг. Если планы пользователя изменились - он может отменить бронирование на уже забронированные им услуги. Бронирование услуг пользователем производится с помощью расписания, которое отображает свободное время выбранного мастера. Все забронированные пользователем услуги отображаются в расписании с учётом даты и времени бронирования, а также продолжительности предоставления услуги.
- Мастер имеет возможность редактировать перечень предоставляемых им услуг, просматривать забронированные пользователями услуги и вид забронированных услуг а также точку предоставления услуг.
- Бизнес-пользователь имеет возможность редактировать список принадлежащих ему точек предоставления услуг, редактировать сами точки предоставления услуг, а также принимать на работу либо увольнять мастеров для каждой из точек.

# Как всё это устроено
Архитектурно проект представляет собой единое Spring Boot приложение.
- В качестве базы данных использована PostgreSQL.
* Слой Persistence - Spring Data JPA
* Слой Security - Spring Security
* Слой отображения - Spring MVC
* Шаблонизатор - Thymeleaf

## Требования для запуска

- Java 11
- Maven

Вся разработка выполнена в среде Intellij IDEA.

## Структура папок проекта
* src
* ├───main
* │   ├───java
│   │   └───com
│   │       └───convenientservices
│   │           └───web
│   │               ├───configurations
│   │               ├───controllers
│   │               ├───dto
│   │               ├───entities
│   │               ├───enums
│   │               ├───exceptions
│   │               ├───mapper
│   │               ├───repositories
│   │               ├───services
│   │               └───utilities
│   │                   └───spec
│   └───resources
│       ├───db
│       │   └───migration
│       ├───static
│       │   ├───img
│       │   └───styles
│       └───templates
│           └───fragments
└───test
    └───java
        └───com
            └───convenientservices
                └───web
                    └───jpa


# Реализованный функционал
На текущий момент создан MVP продукта.

Список выполненных задач:
* Индексная страница ресурса
* Страница авторизации
* Страница регистрации
* Главная страница ресурса
* Страница личного кабинета пользователей
* Созданы необходимые сущности
* Созданы скрипты заливки тестовых данных и создания таблиц БД
* Настроен Маппер для ДТО
* Созданы необходимые сервисы и репозитории
* Настроена Security
* Настроена Email рассылка писем с кодом подтверждения при регистрации пользователей, настроено восстановление паролей
* Настроена проверка полей на страницах регистрации и авторизации
* Настроено оповещение пользователя о неактивированном аккаунте
* Настроена выдача дополнительных полей меню в личном кабинете относительно ролей пользователей
* Настроены дифференцированные поля меню для авторизованных и неавторизованных юзеров.
* Создана страница выбора точек предоставления услуг с фильтрацией
* Создан функционал добавления точки в избранное, создана страница избранных точек
* Создана страница бронирования услуги в виде расписания
* Создана страница просмотра и редактирования забронированных услуг
* Создан личный кабинет для пользователя
* Создан функционал редактирования свойств пользователя
* Создан личный кабинет для мастера
* Создан функционал редактирования предоставляемых услуг мастером
* Создан личный кабинет для бизнес-пользователя
* Создан функционал редактирования точек предоставления услуг для бизнес-пользователя
* Создан функционал приёма на работу мастеров в выбранную точку предоставления услуг бизнес-пользователем
* Создана страница About

