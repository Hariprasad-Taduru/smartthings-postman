# SmartThings Postman Web Client

* Purpose: A web client for accessing SmartThings API's. Supports both staging and production SmartThings environments.

### Technology stack used
	* Spring Boot version 2.3.4.RELEASE
	* Maven version 4.0.0
	* Java version 1.8
	
### What you need to have to run this project:
	* PAT token 
	* Mobile token (optional for now)

### How to run this project:
	* Clone this project 
	* Create file env.config in C folder (in Windows OS), in /tmp/ (in Ubuntu OS) 
	* Add below keys in the file
		* st.stg.token=
		* st.prd.token=
		* st.stg.test.location=
		* st.prd.test.location=

### Want to contribute? Please follow below rules and raise merge request:
	* Logs to be added only in client classes. Strict no for controller, service classes.
	* Generation of loggingId should only be in client classes.
	
### For more details, pls contact author hariprasad.taduru@gmail.com

