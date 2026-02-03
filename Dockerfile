FROM tomcat:9.0-jdk17

# Clean default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Add JDBC driver(s) to Tomcat global classpath
COPY libs/postgresql-42.7.9.jar /usr/local/tomcat/lib/
COPY libs/mysql-connector-j-9.3.0.jar /usr/local/tomcat/lib/

# Copy the war so it runs at /EquiTrack
COPY ROOT.war /usr/local/tomcat/webapps/EquiTrack.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
