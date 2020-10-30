### SmartThings Postman Web Client

Purpose: A web client for accessing SmartThings API's. Supports both staging and production SmartThings environments.

#### Technology stack used
	1. Spring Boot version 2.3.4.RELEASE
	2. Maven version 4.0.0
	3. Java version 1.8
	4. Open API swagger UI version 1.4.6
	5. SmartThings java SDK client version 0.0.4-PREVIEW
	6. Lombok version 1.18.14
	
#### What you need to have to run this project:
	1. Personal Access Token token 
	2. Mobile token (optional for now)

#### How to run this project:
	1. Clone this project 
	2. Create file env.config in C folder (in Windows OS) or in /etc/ (in Ubuntu OS) 
	3. Add below keys in the file
		st.stg.pat.token=
		st.stg.test.location=
		st.acpt.pat.token=
		st.acpt.test.location=
		st.prd.pat.token=
		st.prd.test.location=
	4. Run below commands and run the fatty jar using below command OR load this project in Spring Tool Suite and run the main class.
		./mvnw clean
		./mvnw package
		java -jar target/SmartThings-Postman-0.0.1-SNAPSHOT.jar
	5. Service starts on port 5200
	6. You should see message "Started SmartThingsPostmanApplication in x seconds" in the console for a successfull run.
	6. Launch chrome browser and access http://localhost:5200/swagger-ui.html

#### Want to contribute? Please follow below rules and raise merge request:
	1. Logs to be added only in client classes.
	2. Generation of loggingId should only be in client classes.
	3. Need to update release notes every feature addition.
	
#### For more details, pls contact author hariprasad.taduru@gmail.com

