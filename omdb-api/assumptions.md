_your assumptions when solving the challenge_

1. based on current requirement 
"indicate whether a movie won 'best picture' Oscar " other data in csv file did not use at this time, 
base on this assumption crate single table that contain only best picture category.
this solution help to search in small area of data and increase search throughput. But i know loose some part of data
and could not answer to other category of oscar award. 

1.  I could store only winner but to implement rating requirement, store all movies of best picture category regardless of win or lose.

1. User rate score will be store in database. 
   Find top-rated movies could be done based on average of user's rate for every movies.
   But in CSV did not exist any value for box office value. 
    
    1. But omdbapi provide free API to search movie and view box office value. 
       After found top-rated movie , I search on omdbapi concurrently to access box office value to sort final result.
        
    1. But You know availability and quality of an external or an internal  service is so important to keep quality of our service.
       External service can be unavailable or unaccessable or have huge latency in high load time. To keep quality of our services We should handle these issues:
       Network Connection Exception, Api server down time (updating), Long response time and etc.
       To solve them, try to use circuit breaker pattern. and configuring it to support slow response rate and 
           other exceptions that maybe occurred.
    1. Also in top-rated service we should call an external service at n( for this case n is 10)  times.
               We can do it concurrent. base on Java Concurrency in Practice book (Book by Brian Goetzthis),
               any task in computing have some processing cost and IO cost. 
               If a task need to more cpu clock cycle (solving a mathematics operation)  , we called it CPU bound task,
               and if a task need to wait long time for an IO (reading from disk, or network socket) we called it IO bound task.
               Concurrency solution help to solve both of them. But to find efficient total number of thead 
               of thread pool need some small calculation. 
       For CPU bound task this value should not grater than machine cores,
               but for IO bound task this value should be greater that it.
               this is brian gotz's formula to find optimum number of threads:
        
            CPU Bound Tasks:
                threads = number of CPUs + 1
            
            IO Bound Tasks:
                threads = number of cores * (1 + wait time / service time)
                
                
1. for api key/token checking scenario, I assumed api keys already created and stored in database. at boot of system those items has been done.
       also these keys have expiration time and valid until specific time. also these keys have owner and
       access rights. in my solution implemented authentication and authorization . authentication done 
       by api key parameter in every request and authorization done by every rest call through custom annotation and aspect.
       above every rest web method that we want to check access ,define required access by annotation and remaining checking done
       by aspect(authorization access checker aspect). 
    
    1. to make testing scenario simple, I created 3 different keys with different access to simulate and test all of authentication and authorization scenario correctly. 
    
        1. Developer role key is 'developer_key' and only access to call search rest service
        1. Tester role key is 'tester_key' and able to search a movie and rating it.
        1. Leader role key is 'leader_key' and able to do call all services (searching, rating and viewing top-rated)

1. data model description
    1. movies table that imported from csv
    1. review table that contain rating value and also have foreign key to movie table
    1. service key table contain key,expiration time and owner name
    1. service access table contain defined access in our system
    1. service key access table contain many to many relation between service key and service access
        any service key can have many accesses and also any access can be assign to many key
