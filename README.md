# 📘 Price Service API [Monolithic way] 

> *Система аналітики цін акцій (Price Service) – REST API на Spring Boot для зберігання, запиту та розрахунку
індикаторів на основі часових рядів цін.*

---

## 👤 Автор

* **ПІБ**: Комаров Артем
* **Група**: ФЕС-42
* **Керівник**: доц. КУНЬО І.М
* **Дата виконання**: 07.06.2025

---

## 📌 Загальна інформація

* **Тип проєкту**: Мікросервіс (REST API на Spring Boot)
* **Мова програмування**: Java 17 (Spring Boot 3.4.5)
* **База даних**: MongoDB 7.0
* **Система збірки**: Maven
* **Контейнеризація**: Docker + Docker Compose
* **Фреймворки / Бібліотеки**:

    * Spring Boot (Web, Data MongoDB, Actuator)
    * Lombok, MapStruct
    * Ta4j для розрахунку індикаторів
    * SLF4J (логування)

---

## 🧠 Опис функціоналу

* 🔌 **Status Endpoint**

    * `GET /api/v1/status/ping`
    * Перевірка, що сервіс запущено (повертає HTTP 200 з простим повідомленням).

* 📊 **CRUD для котировок (Price)**

    * `POST /api/v1/prices` – додавання одного чи масиву котировок (сюди потрапляють дата, тикер, ціна).
    * `GET /api/v1/prices?ticker={symbol}&from={date}&to={date}` – отримання котировок певного тикера в діапазоні дат.
    * `DELETE /api/v1/prices/{id}` – видалення конкретної котировки за її `ObjectId`.

* 📈 **Аналітика (Technical Indicators)**

    * `GET /api/v1/analytics/sma?ticker={symbol}&period={n}` – розрахунок Simple Moving Average (SMA) для тикера за
      останні `n` періодів.
    * `GET /api/v1/analytics/rsi?ticker={symbol}&period={n}` – розрахунок Relative Strength Index (RSI) за вказаним
      періодом.
    * Результат повертається як JSON з масивом дат та значень індикатора.

* ⚙️ **Завантаження початкових даних**

    * При старті сервісу зчитуються початкові дані з папки `src/main/resources/data` (наприклад, CSV з котировками) і
      зберігаються в MongoDB.

* 📑 **Побудова динамічних запитів**

    * Функціонал парсингу фільтрів із рядка запиту (оператори `>`, `<`, `>=`, `<=`, `==`) для більш гнучкого пошуку
      даних.

---

## 🧱 Опис основних класів / файлів

| Клас / Файл                                                   | Призначення                                                                                               |
|---------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| `PriceServiceApplication.java`                                | Точка входу Spring Boot, автоматичне підключення до MongoDB, налаштування репозиторіїв.                   |
| `StatusController.java`                                       | REST-контролер для `/api/v1/status` (перевірка стану сервісу).                                            |
| `PricesController.java`                                       | REST-контролер для CRUD операцій з котировками (POST, GET, DELETE).                                       |
| `AnalyticController.java`                                     | REST-контролер для API розрахунку технічних індикаторів (SMA, RSI).                                       |
| `PriceService.java` / `PriceServiceImpl.java`                 | Бізнес-логіка: операції з котировками (збереження, пошук, видалення).                                     |
| `Ta4jHeavyService.java`                                       | Сервіс для розрахунку технічних індикаторів за допомогою бібліотеки Ta4j.                                 |
| `Price.java`                                                  | Модель (Entity) для збереження котировок у MongoDB (`@Document` з полями `ticker`, `priceDate`, `value`). |
| `PriceDto.java`                                               | DTO для передачі даних котировки через REST (без MongoDB-анотацій).                                       |
| `PriceRepository.java`                                        | Spring Data MongoDB репозиторій для `Price` (автоматичні CRUD-методи).                                    |
| `SMAResult.java` / `IndicatorResult.java`                     | Моделі для структурування результатів індикаторів (дата + значення).                                      |
| `DataBaseLoader.java`                                         | Компонент, що при запуску завантажує початкові дані з ресурсів у MongoDB (якщо є).                        |
| `QueryBuilder.java`, `FilterParser.java`, `FilterClause.java` | Утиліти для побудови динамічних MongoDB-запитів за фільтрами, заданими в URL.                             |
| `EntityDtoMapper.java` / `PriceMapper.java`                   | Мапінг між Entity (Price) і DTO (PriceDto) через MapStruct.                                               |
| `TickerPriceProjection.java`                                  | Інтерфейс-прожекція для вибіркових полів (наприклад, тільки `ticker` та `closePrice`).                    |
| `Operator.java`                                               | Перелічення операторів порівняння (`>`, `<`, `==`, тощо) для фільтрації.                                  |
| `FilterParser.java`                                           | Клас для розбору рядка фільтру (наприклад, `price>100`) у окремі умови.                                   |
| `BaseController.java`                                         | Базовий контролер (спільна логіка) для інших REST-контролерів (наприклад, обробка винятків).              |

---

## ▶️ Як запустити проєкт "з нуля"

> Дані вантажуться автоматично при запуску застосунку

### 1. Встановлення інструментів

* Java 17 (JDK)
* Maven 3.8+
* Docker (для контейнеризації)
* Docker Compose 1.29+
* MongoDB 7.0 (за необхідності локально, але Docker Compose автоматично підніме контейнер)

### 2. Клонування репозиторію

```bash
git clone https://github.com/your-user/analytics-microservices.git
cd analytics-microservices
```

### 3. Налаштування `application.yaml`

* За замовчуванням (без профілю) сервіс слухає на порту **9002** і підключається до
  `mongodb://localhost:27017/price-db`.
* Для запуску в Docker використовується профіль `docker`, який слухає на порті **8080** і підключається до контейнеру
  Mongo за іменем `mongo`.
* Якщо хочете запускати локально, перевірте, що у вас MongoDB працює на `localhost:27017`.

### 4. Налаштування середовища (опціонально)

Якщо ви хочете передавати MongoDB URI чи порт через змінні оточення, створіть файл `.env` у корені з таким вмістом:

```
SPRING_PROFILES_ACTIVE=docker
SPRING_DATA_MONGODB_HOST=mongo
SPRING_DATA_MONGODB_PORT=27017
```

### 5. Побудова та запуск через Docker Compose

```bash
# З кореневої папки проєкту (analytics-microservices):
docker-compose up --build
```

* **Mongo** (порт 27017) і **mongo-express** (порт 8081) піднімуться автоматично.
* **api-service** (Price Service) стартує на порту **8080**.
* Щоб перевірити статус сервісу, у браузері або через `curl` виконайте:

  ```
  curl http://localhost:8080/api/v1/status/ping
  ```

  Повернеться HTTP 200 з повідомленням, що сервіс запущено.

### 6. Запуск без Docker (локально)

```bash
# Переконайтесь, що MongoDB запущено локально на localhost:27017
cd api-service
mvn clean package -DskipTests
java -jar target/api-service-1.0-SNAPSHOT.jar
```

* Сервіс почне слухати на порті **9002** (за замовчуванням).
* Перевірте за адресою: `http://localhost:9002/api/v1/status/ping`.

---

## 🔌 API приклади

### 🔐 Статус сервісу

**GET /api/v1/status**

```bash
curl -i http://localhost:8080/api/v1/status/ping
```

**Response:**

```json
pong
```

---

### 📋 Ціни (Prices)

* **POST api/public/v1/prices/load-data** – Додавання цін

  ```json
    [
        {
            "ticker": "AAPL",
            "date": "2010-01-01",
            "open": 294.06,
            "high": 340.68,
            "low": 268.59,
            "close": 329.72,
            "volume": 6459.53
        }
    ]
  ```

  **Response:** HTTP 201 CREATED

* **GET api/public/v1/prices?date=gte:2015-03-30&sort=date,asc&page=0&size=2&ticker=eq:AAPL** – Отримати цін AAPL у
  діапазоні
  **Response:**

  ```json
  {
    "content": [
        {
            "ticker": "AAPL",
            "date": "2024-01-01",
            "open": 300.16,
            "high": 344.56,
            "low": 273.32,
            "close": 311.81,
            "volume": 20138.96
        },
        {
            "ticker": "AAPL",
            "date": "2024-01-02",
            "open": 384.93,
            "high": 397.28,
            "low": 367.35,
            "close": 378.16,
            "volume": 87904.84
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 2,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": false,
    "totalPages": 262,
    "totalElements": 523,
    "size": 2,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false

}

* **GET /api/public/v1/prices?date=gte:2015-03-30\&sort=ticker,desc\&page=0\&size=2**
  Приклад запиту з фільтрацією за датою, сортуванням за тикером у порядку спадання та пагінацією (перша сторінка, 2
  записи на сторінку):

  ```bash
  curl -i "http://localhost:8080/api/public/v1/prices?date=gte:2015-03-30&sort=ticker,desc&page=0&size=2"
  ```

**Response (200 OK):**

  ```json
  {
  "content": [
    {
      "ticker": "XOM",
      "date": "2025-12-31",
      "open": 246.3,
      "high": 294.38,
      "low": 205.78,
      "close": 216.83,
      "volume": 92042.38
    },
    {
      "ticker": "XOM",
      "date": "2025-12-30",
      "open": 496.86,
      "high": 536.79,
      "low": 479.5,
      "close": 481.35,
      "volume": 19169.34
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 26150,
  "totalElements": 52300,
  "size": 2,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
  ```

* **DELETE /api/v1/prices/{id}** – Видалити котировку за `id`
  **Response:** HTTP 204 NO CONTENT

---

### 📈 Аналітика (Indicators)

* **GET api/public/v1/prices/indicators/historical/AAPL?window=300**
  Повертає масив об’єктів `{ "date": "...", "sma": ... }` для SMA із періодом 5.

  **Response (JSON):**

  ```json
  [
    {
        "ticker": "AAPL",
        "date": "2025-02-21",
        "sma": 271.42
    },
    {
        "ticker": "AAPL",
        "date": "2025-02-24",
        "sma": 271.64
    }
  ]
  ```

* **GET api/public/v1/prices/indicators?tickers=AAPL&start=2000-01-01&end=2025-01-01**
  Обчислює метрики для AAPL.

  **Response (JSON):**

  ```json
  [
    {
        "ticker": "AAPL",
        "atr": 187.71,
        "rsi": 45.607227335893825,
        "bbUpper": 335.0,
        "bbLower": -152.0,
        "maxDrawdown": -0.9193563582235789,
        "recoveryTimeBars": 0
    }
  ]
  ```

## 🧪 Проблеми і рішення

| Проблема                           | Рішення                                                                                             |
|------------------------------------|-----------------------------------------------------------------------------------------------------|
| 500 Internal Server Error при POST | Перевірити, що поле `priceDate` відповідає формату ISO 8601 (наприклад, `2025-06-06T00:00:00Z`).    |
| Не підключається до MongoDB        | Перевірити, що змінна середовища `SPRING_DATA_MONGODB_HOST=mongo` (або localhost, якщо без Docker). |
| Помилка при фільтрації даних       | Перевірити правильність виразу `filter` (наприклад, `price>100`) і налаштування `FilterParser`.     |

---

## 🧾 Використані джерела / література

* Spring Boot офіційна документація ([https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot))
* MongoDB документація ([https://docs.mongodb.com/](https://docs.mongodb.com/))
* Ta4j (Technical Analysis for Java) ([https://ta4j.github.io/](https://ta4j.github.io/))
* MapStruct ([https://mapstruct.org/](https://mapstruct.org/))
* Lombok ([https://projectlombok.org/](https://projectlombok.org/))
