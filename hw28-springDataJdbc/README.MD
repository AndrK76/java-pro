## Дополнительно к ДЗ

### Кроме сервиса работающего через Spring JDBC есть простой сервис работающий через HashMap
Выбор используемого сервиса через параметр настройки services.clientService
- inmemory
- jdbc

Для возможности запуска приложения с inMemory сервисом без доступной БД так же отключена 
автоконфигурация для DataSourceAutoConfiguration, DataSource конфигурируются вручную в DataSourceConfig

### Реализован API контроллер
1. Web-интерфейс через Ajax (JQuery)
2. Swagger-документация
3. Настройка доступа CORS в конфиге (api.cors-allow-any)

