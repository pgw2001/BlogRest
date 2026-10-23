# ── 1단계: 빌드 ────────────────────────────────────
# 빌드와 실행 모두 build.gradle의 toolchain(Java 21)과 같은 버전을 씁니다.
# 다르면 Gradle이 이미지 안에서 Java 21을 찾지 못해 빌드가 멈춥니다.
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 먼저 빌드 설정만 복사해서 의존성을 내려받습니다.
# 소스만 바뀌었을 때 이 단계를 건너뛰게 하려는 것입니다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 이제 소스를 복사하고 실행 파일을 만듭니다.
# 테스트는 배포 전에 내 컴퓨터에서 돌립니다. 여기서는 건너뜁니다.
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# ── 2단계: 실행 ────────────────────────────────────
# 실행에는 컴파일러가 필요 없으므로 jdk 가 아니라 jre 를 씁니다.
FROM eclipse-temurin:21-jre
WORKDIR /app
# jar 이름은 settings.gradle의 rootProject.name(blog)과 version으로 정해집니다.
COPY --from=build /app/build/libs/BlogRest-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
