FROM openjdk:21

WORKDIR /app/todo

COPY ./build/libs/*.jar /app/todo/todo.jar

ENTRYPOINT ["java", "-jar", "todo.jar", "--spring.profiles.active=prod"]