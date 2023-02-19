# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="werghjkl333@naver.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 49005 available to the world outside this container
EXPOSE 49005

# The application's jar file
ARG JAR_FILE=build/libs/sunygo-0.0.1-SNAPSHOT.war

# Add the application's jar to the container
ADD ${JAR_FILE} AlgorithmApp.war

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-war","/to-do-springboot.war"]
