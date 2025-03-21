FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем JAR-файл приложения
COPY target/mentorlik-backend-0.0.1-SNAPSHOT.jar /app/mentorlik-backend.jar

# Открываем порт
EXPOSE 8080

# Запуск приложения с дополнительными параметрами для отладки
CMD ["java", "-Dspring.profiles.active=dev", "-Dlogging.level.root=INFO", "-Dlogging.level.com.mentorlik=DEBUG", "-Dlogging.level.org.springframework.security=DEBUG", "-Dlogging.level.org.hibernate.SQL=DEBUG", "-jar", "/app/mentorlik-backend.jar"]