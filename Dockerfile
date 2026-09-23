FROM ://amazonaws.com

LABEL maintainer="devops@wakanda.academy"

WORKDIR /app

COPY target/wakacop-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["/app/startup.sh", "-jar", "wakacop-0.0.1-SNAPSHOT.jar"]
