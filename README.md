# ui-testcontainers-demo
Демо-приложение с применением Testcontainers для UI автотестов

### Используются следующие контейнеры:
- BrowserWebDriverContainer (Selenium для UI тестирования)
- MockServerContainer (для заглушек внешнего сервиса)

### Требования для запуска:
- Java 11+
- Docker

> Перед запуском тестов необходимо сначала запустить само тестируемое приложение `TestcontainersDemoApplication`

### Полезные ссылки по Testcontainers
- https://www.testcontainers.org/
- https://youtu.be/r5dA_g-_N6M - Введение в Testcontainers. Прокачиваем интеграционные тесты
- https://youtu.be/PEVVvZOt7bY - Сергей Егоров — TestContainers — интеграционное тестирование с Docker
- https://youtu.be/xgZ8KyUDjvQ - Сергей Егоров — Testcontainers: Год спустя
