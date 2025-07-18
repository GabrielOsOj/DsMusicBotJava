FROM openjdk:20-jdk-slim
ARG JAR_FILE=target/DiscordJavaBotV2-v2.0.jar
COPY ${JAR_FILE} discordBot.jar

CMD ["echo", "Instalando dependencias"]

RUN apt-get update && \
    apt-get install -y python3 python3-pip ffmpeg && \
    pip3 install --no-cache-dir yt-dlp


ENTRYPOINT [ "java", "-jar", "discordBot.jar" ]