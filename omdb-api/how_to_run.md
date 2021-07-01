_A short explanation about how to run the solution with all the
needed parts_

1. Extract zip file of project 

1. open terminal (command prompt in windows)

1. Be sure your Java home and maven home environment variables defined correctly.

    1. execute "**java -version**" in terminal. If every things was good, you should see java version 
    1. Our project based on **java 11**
    1. execute "**mvn -version**" in terminal. If every things was good, you should see java version

1. change current directory to project folder where the "pom.xml" file exist.

1. execute "mvn clean package" command in terminal.

1. change current directory to target folder.

1. execute "java -jar omdb-api-0.1.jar" in terminal. 

    1. By default Our web application will be execute on port 8080 in your system.
        if you would like to change execution port using -Dserver.port={Your desired port}
        You can ser it to 0 to execute on at least one free port on your system to prevent port used issues.

    1.  to execute on custom port 9090, first stop old execution and then try 
        execute "java -Dserver.port=9090 -jar omdb-api-0.1.jar"
        
1. By default application at boot time import "**academy_awards.csv**" to database.

    1. I use H2 database that store data in memory. All data exist during running of application,
        but will be lost after stopping the application.
    
    1. H2 web console is enable by default and can be accessible based on execution port of application.
        if I the application are running on port 8080, to access web console Of H2,
        Open your web browser (safari, chrome, firefox or edge) and enter following address
        "**http://localhost:8080/academy-awards/h2-console**"
    
    1. Enter following data in login page of H2 web console 
        1. Saved Setting: **Generic H2(Embedded)**
        1. Setting Name: **Generic H2(Embedded)**
        1. Driver Class: **org.h2.Driver**
        1. JDBC URL: **jdbc:h2:mem:omdb**
        1. User Name: **sa**
        1. Password: 
        1. Press **Connect** button. 
        
1. If you want to prepare test data for rating section to test
    top-rated service, start application with following parameter
    this parameter create 1000 random rating and help to test.  
    
        -Dgenerate-random-review-count=1000 