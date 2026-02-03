FROM tomcat:9.0-jdk17

# Clean default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Add JDBC driver(s) to Tomcat global classpath
COPY libs/postgresql-42.7.3.jar /usr/local/tomcat/lib/
COPY libs/mysql-connector-j-9.3.0.jar /us

# Copy the war as ROOT so it runs at /
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
