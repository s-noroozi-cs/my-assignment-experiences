file explaining what needs to be done to use the movieService.

I assumption the application are running in your local host machine 
    and will be accessible on port 8080.
    
based on assumptions.md file, We have three api key that can be use.
api key have access and system control it. 
    
    key: develoer_key
    scope: only call search web service
    
    key: tester_key
    scope: call search and rating web services
    
    key: leader_key
    scope: call all web services
    
open terminal and be sure curl tool installed and works correctly.
   for example execute "curl --version" to see your curl version.(if installed)
   
To view If specific movie (search by title - ignore case) 
    won oscar award in category of "Best Picture" 
    execute following command in terminal.
    
        curl -X GET "http://localhost:8080/academy-awards/api/movies?apikey=developer_key&t=crash"

Default response format is json and for above request based on your CSV
    response will be like as blew:
    
        {
            "id": 38,
            "year": "2005 (78th)",
            "title": "Crash",
            "additionalInfo": "Paul Haggis and Cathy Schulman, Producers",
            "won": "YES",
            "response": true
        }

To change response format to xml try to execute following command in terminal
    
        curl -X GET "http://localhost:8080/academy-awards/api/movies?apikey=developer_key&t=crash&r=xml" 

response format will be xml as same as blew content.         
    
       <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
       <movie response="true">
            <id>38</id>
            <year>2005 (78th)</year>
            <title>Crash</title>
            <additionalInfo>Paul Haggis and Cathy Schulman, Producers</additionalInfo>
            <won>YES</won>
        </movie>

To rate a movie you should be get movie id. To do that using above api (search) that know movie's id.
    Then you can rate and write a comment to a movie. 
    Assumption: We want to rate Crash movie and it's id is 38. Execute following command in your terminal.
    
        curl -X POST -H "content-type: application/json" "http://localhost:8080/academy-awards/api/movies/38/reviews?apikey=tester_key&r=xml" -d '{"rating":8}' 
        
Response of above request:
        
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <root response="true">
            <id>1004</id>
            <rating>8</rating>
        </root> 

If you does not specify response format or define it as json result maybe like this:

        curl -X POST -H "content-type: application/json" "http://localhost:8080/academy-awards/api/movies/38/reviews?apikey=tester_key&r=json" -d '{"rating":8}'

Response like this:  
        
        {
            "id":1005,
            "rating":8,
            "response":true
        } 

To fetch 10 top-rated movies,

    curl -X GET "http://localhost:8080/academy-awards/api/movies/top-rated?apikey=leader_key"

Response like this: 

        {
            "response": true,
            "movies": [
                {
                    "id": 37,
                    "year": "2005 (78th)",
                    "title": "Capote",
                    "additionalInfo": "Caroline Baron, William Vince and Michael Ohoven, Producers",
                    "won": "NO"
                },{
                    "id": 40,
                    "year": "2005 (78th)",
                    "title": "Munich",
                    "additionalInfo": "Kathleen Kennedy, Steven Spielberg and Barry Mendel, Producers",
                    "won": "NO"
                },{
                    "id": 71,
                    "year": "1998 (71st)",
                    "title": "Elizabeth",
                    "additionalInfo": "Alison Owen, Eric Fellner and Tim Bevan, Producers",
                    "won": "NO"
                },{
                    "id": 168,
                    "year": "1979 (52nd)",
                    "title": "Breaking Away",
                    "additionalInfo": "Peter Yates, Producer",
                    "won": "NO"
                },{
                    "id": 199,
                    "year": "1973 (46th)",
                    "title": "The Sting",
                    "additionalInfo": "Tony Bill, Michael Phillips and Julia Phillips, Producers",
                    "won": "YES"
                },{
                    "id": 223,
                    "year": "1968 (41st)",
                    "title": "Oliver!",
                    "additionalInfo": "John Woolf, Producer",
                    "won": "YES"
                },{
                    "id": 360,
                    "year": "1942 (15th)",
                    "title": "The Pied Piper",
                    "additionalInfo": "20th Century-Fox",
                    "won": "NO"
                },{
                    "id": 421,
                    "year": "1936 (9th)",
                    "title": "Romeo and Juliet",
                    "additionalInfo": "Metro-Goldwyn-Mayer",
                    "won": "NO"
                },{
                    "id": 425,
                    "year": "1936 (9th)",
                    "title": "Three Smart Girls",
                    "additionalInfo": "Universal",
                    "won": "NO"
                },{
                    "id": 475,
                    "year": "1929/30 (3rd)",
                    "title": "Disraeli",
                    "additionalInfo": "Warner Bros.",
                    "won": "NO"
                }
            ]
        }
        
You can also limit top-rated list to specific number, for example 3:

    curl -X GET "http://localhost:8080/academy-awards/api/movies/top-rated?apikey=leader_key&l=3&r=xml"

response like this: 
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <movies response="true">
        <movie>
            <id>37</id>
            <year>2005 (78th)</year>
            <title>Capote</title>
            <additionalInfo>Caroline Baron, William Vince and Michael Ohoven, Producers</additionalInfo>
            <won>NO</won>
        </movie>
        <movie>
            <id>211</id>
            <year>1970 (43rd)</year>
            <title>Airport</title>
            <additionalInfo>Ross Hunter, Producer</additionalInfo>
            <won>NO</won>
        </movie>
        <movie>
            <id>278</id>
            <year>1957 (30th)</year>
            <title>Sayonara</title>
            <additionalInfo>William Goetz, Producer</additionalInfo>
            <won>NO</won>
        </movie>
    </movies>