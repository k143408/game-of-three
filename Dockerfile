FROM amazoncorretto:21.0.2-alpine as build
WORKDIR /workspace/app

# Copy Gradle files
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle gradle

COPY . .

RUN ./gradlew build -x test

# Extract the application JAR
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/got-0.0.1-SNAPSHOT.jar)

FROM amazoncorretto:21.0.2-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.exercise.got.GotApplication"]
