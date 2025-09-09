FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app
WORKDIR /app
RUN addgroup --system javauser && adduser --system --shell /bin/false --ingroup javauser javauser
COPY target/*SNAPSHOT.jar app.jar
COPY ddagent/* ./
RUN chown -R javauser:javauser /app
USER javauser
ENV JAVA_OPTS=""
CMD java "-Djava.security.egd=file:/dev/./urandom"  $JAVA_OPTS -jar app.jar
