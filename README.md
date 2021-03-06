# YelpClone-ish

## Overview

Developed in VSCode with Spring Boot. Final assignment in **CSCI 201 - Software Engineering**. Topics covered: api integration, front-end javascript, back-end java, MySQL database persistence. Integrates Google OAuth2 and custom username/password verification with use of *BCrypt* for secure password hashing.

MySQL database hosted by me on DigitalOcean virtual machine.

#### To open in Eclipse:
1. Unzip saleatsbackend.zip
2. In Eclipse, navigate to *file->import->Maven->Existing Maven Projects*
3. Select unzipped **assignment-3-abalasky** folder as root directory
4. Ensure *pom.xml* is checked
5. Select finish when prompted.
6. Saleatsbackend will appear as a project in eclipse and the applicaiton can be run
7. Go to the base package *src/main/java -> io.balasky.saleatsbackend* and right click *Run As Java Application* SaleatBackendApplication.java.
8. A tomcat server will start on *localhost:8081*. If neccessary change the default port in *src/main/resources/application.properties*.

#### Note on MySQL Database:
You do not need to set up a local database as it will automatically connect to my database hosted on digital ocean. You can find the details of the connection in *application.properties*.
