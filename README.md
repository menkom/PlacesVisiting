# Создать сервис Хронология, для сохранения посещенных мест

1. Работа с пользователем:
   * Регистрация новых пользователей (или вход через гугл аккаунт).
   * Предоставление возможности планировать поездки.
   * Предоставление возможности добавления прошедших поездок.
   * Предоставление возможности совместных поездок с другими пользователями.
   * Редактирование информации (изменение даты и списка людей).
   * Возможность удалить планируемую поездку.
   * Возможность поделиться своей картой с друзьями.
2. Работа с картой и поездками:
   * Хранение информации о посещенных местах.
   * Получение списка всех мест (фильтрация по периоду, стране, людям).
   * Вывод нескольких фотографий мест (посещенных или которые еще будут посещены).
   * Добавление планируемых поездок в гугл календарь (отправка писем на почту при нескольких участниках).
3. Работа с уведомлениями:
   * Добавление пуш-уведомлений для различных уведомлений.
   * Отправка уведомлений о планируемых поездках.
4. Асинхронная обработка:
   * Добавление асинхронной обработки для выполнения **задач**, которые могут занимать продолжительное время. (Какие это могут быть задачи? Может стоит это обдумывать, когда будет конкретная задача, а не пытаться объять необъятное?)

### Questions
1. Работа с пользователями. У нас предполагается наличие обычно зарегистрированных пользователи и авторизованных через Google. 

   Q1: Пользователь mike@gmail.com, вошедший через Google и ранее зарегистрированный пользователь mike@gmail.com  это один и тот же?

   A1: да
 
2. >Предоставление возможности совместных поездок с **другими пользователями**.

   Q1: Это предоставление возможности редактировать поездку или это просто список друзей?

   А1: Как в Airbnb. Ты букаешь, создаешь поездку и шаришь ее другим. Максимум оставление заметок со стороны других пользователей. На данном этапе

3. >Возможность удалить **планируемую** поездку.

   Q1: Только планируемую можно удалять?

   А1: Ты сделал драфтом, и в итоге не поехал. И такой: ну и ладно. Удалю тогда из записи

4. >Возможность поделиться своей **картой** **с друзьями**.

   Q1: Что значит КАРТА? Это список или реально карта maps.google.com?

   А1: Изначально предполагалось делиться точками на карте гугл

   Q2: Что имеется в виду под словом "друзья"? Это те, кто едут со мной или просто ссылка кому-либо?

   А2: Те, кто едут с тобой, попутчики

   Q3: Что значит поделиться? Поделиться возможностью редактироваться или просто просмотреть запланированные места?

   А3: Поделиться - пошарить запланированные места, максимум оставить заметку\комментарий

5. >Хранение информации о посещенных **местах**.

   Q1: Что мы подразумеваем под словом "место"? Это должны быть чёткие координаты или мы можем ограничиться словесным названием?

   А1: Подразумевалось, что вот ты накидал план поездки и сохранил. Например, поставил галочки и заметки о посещенном месте. Но в целом на твой выбор. Если сможешь это на карте отобразить координатами и описанием - огонь, нет - ограничься словесным описанием.

6. >Вывод **нескольких** фотографий мест (посещенных или которые еще будут посещены).

   Q1: Как должен формироваться этот список? Просто рандомно?

   А1: Список мест - по плану посещения. Сами фотки - как хочешь - можешь по времени загрузки фоток

7. >Добавление планируемых поездок в гугл календарь (отправка писем на почту при **нескольких** участниках).

   Q1: Если только один участник, то нельзя отправлять?

   А1: Можно. Так происходит, когда билеты на самолет покупаешь, например.

   Q2. Список берём из перечисленных в поездке или этот список именно для рассылки?

   А2: Участники поездки

8. >Отправка уведомлений о **планируемых** поездках.

   Q1: Предполагается это будет использоваться на фронте и какая-то всплывашка?

   А1: На твое усмотрение. Я бы наверное сделала бы или пуш или емейлы.

   Q2: Уведомление за какое время должно приходить?

   А2: По настройке пользователя. Но по умолчанию - за неделю и за 3 дня до

9. >Добавление асинхронной обработки для выполнения **задач**, которые могут занимать продолжительное время.

   Q1: Какие это могут быть задачи?

   А1: Например, загрузка тех же фоток, когда они весят как самолет или видосов. Или нотификашек отправки.

   Q2: Может стоит это обдумывать, когда будет конкретная задача, а не пытаться объять необъятное?

   А2: Хорошее замечание


### Project entities:
* Account (User)
* Trip
* Place
* Picture

### Expected endpoints according to task description:
- Account
  * register new user (account)
- Trip
  * register trip (expected or past)
  * add other travellers (users) to your trip
  * edit trip data
       + travellers list
       + date
  * delete expected trip (only planned one, not past)
  * share trip map with friends
  * add trip to Google Calendar (send email to several travellers)
- Place
  * store information about visited places
  * get list of visited places with filters (period, country, people)
  * display photos of several places (visited or planned)

### Additional features
- Notifications
  * Push messages
  * Send push messages about planned trips
- Asynchronous actions
  * Add asynchronous processing for tasks that may take a long time

### Steps of development:
1. Create simple running Spring Boot application
2. Create user model, repository and service for login
3. Create config, service and controller for authentication
4. Add some endpoints to test
5. Implement JWT token generation and validation
6. Add new user account creation and exception handling
7. Create filter to validate user authentication using header token (JWT filter)
8. Add JwtFilter in config
9. Add OAuth Google authentication and let basic and OAuth work together


After getting answers to questions for assignment points application architecture looks completely different. It needs to be split into microservices.
1. Authentication/authorization service
    * only login users can view shared trips - Why? Probably only login users from invitational list can comment but all other can see
2. Scheduler service
    * Send notifications according to planned time before trip (one week before by default) - this means should be 
      1. Different period time
      2. Several periods to select 
3. Image storage
   * Limit file size
   * Use Minio for local development
4. Post upload action - asynchronous job
   * add stamp or watermark on pictures (this way we can satisfy point 4)
5. Notification service (notification service can create such task with specified type of message)
   * Email 
   * Push message
   * Telegram (but this is another field in user description that was not planned at the moment)

### Google Calendar Event
Application sends in email link to add event to Google Calendar. This solution suites in case when you need to use Google Calendar only. There is option to generate .ics file (iCalendar) and attach it to email. This solution is more universal but all value are set. 

iCalendar examples and libraries
* [Managing Calendar and Invites in Java](https://thinkuldeep.com/post/java-calendar-and-invite/)
* [iCal4j - modifying or creating new iCalendar](https://ical4j.sourceforge.net/introduction.html)
* [[Stack Overflow] Best iCalendar library for Java?](https://stackoverflow.com/questions/33901/best-icalendar-library-for-java)

Folder `scratches` contains http requests to different project services.

To use services on frontend all running API can be accessed on http://localhost (no need to search for port)

API description:
* Auth Service API: http://localhost:8079/api/swagger-ui/index.html
* Trip Service API: http://localhost:8080/api/swagger-ui/index.html
* Image Service API: http://localhost:8082/api/swagger-ui/index.html 

# Project start

You can start project by running only one bash file in docker folder:

```shell
cd ./docker/ &&
  bash ./start_from_scratch.sh
```

Probably system will need some small tunning but mostly command is enough.