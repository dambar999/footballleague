FROM java:8
ADD build/libs/*.jar footballleaguedockerapp.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar","footballleaguedockerapp.jar"]

###build image
# docker build -t footballleaguedockerapp .
### run image as container
# docker run -p  8888:8888 footballleaguedockerapp