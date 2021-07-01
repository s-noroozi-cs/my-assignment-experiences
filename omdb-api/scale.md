a description of how it will scale when the number of
users/agents/consumers grows from 100 per day to 10000000 per day, and what
changes would have to be made to keep the same quality of movieService


1. Caching some part of result, such as top-rated movies. 
    When our data growth and also rate of concurrent request of our service api growth too,
    We need to decrease latency of our api.

1. using redis to improve rating request and sending batch update request to database.

1. our solution is state less and can be easily scaled up and work behind any proxy.
      