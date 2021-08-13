Feature: Rest Web Service to update existing stock

  Scenario Outline: Client call web service to updating existing stock
    Given I have the following stocks in system
      | 1 | GOOG | 200.75 |
      | 2 | AMZN | 150.75 |
    When  to update existing stock call service with method: <method> url: <url> content-type: <content-type> and body: <body>
    Then  status code of response is <status-code>
    And   if <status-code> equal 200 then validate response last updating time

    Examples:
      | method | url             | content-type     | body                                  | status-code |
      | PUT    | /api/stocks/1   | application/json | {"name":"GOOG","currentPrice":100.25} | 200         |
      | PUT    | /api/stocks/2   | application/json | {"name":"AMZN","currentPrice":250.25} | 200         |
      | PUT    | /api/stocks/3   | application/json | {"name":"AMZN","currentPrice":250.25} | 404         |
      | POST   | /api/stocks/2   | application/json | {"name":"AMZN","currentPrice":250.25} | 405         |
      | PUT    | /api/stocks/abc | application/json | {"name":"AMZN","currentPrice":250.25} | 400         |
      | PUT    | /api/stocks/1   | application/json | {}                                    | 400         |
      | PUT    | /api/stocks/1   | application/json | {"name":"","currentPrice":250.25}     | 400         |
      | PUT    | /api/stocks/1   | application/json | {"name":"GOOG","currentPrice":0}      | 200         |
      | PUT    | /api/stocks/1   | application/json | {"name":"GOOG","currentPrice":-100}   | 400         |
      | PUT    | /api/stocks/1   | application/json | {"name":"GOOG","currentPrice":"xxx"}  | 400         |
      | PUT    | /api/stocks/1   | application/xml  |                                       | 415         |
      | PUT    | /api/stocks/1   | application/text |                                       | 415         |
      | PUT    | /api/stocks/1   |                  |                                       | 415         |