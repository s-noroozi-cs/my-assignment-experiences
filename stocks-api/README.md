# stocks-api
Simple Stocks API

## To execute backend tests

1. change your working directory to backend

2. open command line / terminal

3. execute following command

		mvn clean test
		

## To start backend service

1. change your working directory to backend

2. open command line / terminal

3. execute following command

		mvn spring-boot:run
		

## To veiw api document based on open api standard

1. first of all start backend service

2. open following link in your bowser

		http://localhost:8080/api-docs
		
## To view api document based on swagger ui

1. first of all start backend service

2. open following link in your bowser

		http://localhost:8080/swagger-ui
		
## To start fronend ui

1. change your working directory to frontend

2. open command line / terminal

3. execute following command

		npm install

4. then execute 

		npm start
		
5. if your default browser does not open automatically, open following link in your browser

		http://localhost:3000/
		
6. by default you see home page, to see stock list, click stocks link and enjoy simple crud ui


## To start both backend and frontend together using docker
 
 1. change your working directory to root of project
 
 2. based on your operation system (windows or linux like) choose bat or sh script 
 
 3. Windows Operation System execute following script
 		
		make-docker-image-and-run.bat
		
4. Linux like Operation System execute following script

		make-docker-image-and-run.sh
		
5. Open your browser and enter following link

		http://localhost:3000
		
6. Enjoy docker facility.
