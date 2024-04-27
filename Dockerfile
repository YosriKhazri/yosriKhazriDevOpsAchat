FROM openjdk:17-jdk
EXPOSE 8082
ADD target/achat-devops-1.0.jar achat-devops-1.0.jar
ENTRYPOINT ["java","-jar","/achat-devops-1.0.jar"]