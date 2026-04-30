# Imagen base con Java 21 para ejecutar la API
FROM eclipse-temurin:21-jdk

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/*.jar app.jar

# Puerto interno de la API en perfil prod
EXPOSE 8085

# Ejecutamos la aplicación usando el perfil de producción
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]