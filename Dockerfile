#imagem base (jdk - 8)
FROM openjdk:8-jdk-alpine
# criar usuario grupo spring (com root)
RUN addgroup -S spring && adduser -S spring -G spring
# usar usuario spring
USER spring:spring
# criar argumento jar_file(coloca o que é compilado para esse argumento)
ARG JAR_FILE=target/*.jar
# vai copiar o arquivo como o arquivo app.jar
COPY ${JAR_FILE} app.jar
# o comando que será executado no container
# ENTRYPOINT ["java", "-jar", "/app.jar"]
#por conta do limite de memoria do heroku, mando parametro de memoria (512m - rodar no maximo 512mega)
ENTRYPOINT ["java", "-Xmx512m", "-Dserver.port=${PORT}","-jar", "/app.jar"]