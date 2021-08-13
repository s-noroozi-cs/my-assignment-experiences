Feature: Rest Web Service to create new stock

  Scenario Outline: Client call web service to create new stock
    Given there are exist only "AAPL" and "AMZN" stocks and call service to create,api with content type is application/json
    When  to create stock call service with method: <method> url: <url> and body: <body>
    Then  header status code is <status-code>
    And   control and validate response with <success> and <status-msg>

    Examples:
      | method | url           | body                                      | status-code | status-msg         | success | description                     |
      | POST   | /api/stocks   | {"name":"PAYCONIQ","currentPrice":100.25} | 201         | CREATED            | true    | ---                             |
      | POST   | /api/stocks   | {"name":"GOOG","currentPrice":100.25}     | 201         | CREATED            | true    | ---                             |
      | POST   | /api/stocks   | {"name":"AAPL","currentPrice":100.25}     | 400         | BAD_REQUEST        | false    | unique constraint on name field |
      | POST   | /api/stocks   | {"name":"AMZN","currentPrice":100.25}     | 400         | BAD_REQUEST        | false    | unique constraint on name field |
      | POST   | /api/stocks   |                                           | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {}                                        | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {                                         | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {"name":"PAYCONIQ"}                       | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {"currentPrice":100.25}                   | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {"name":"PAYCONIQ","currentPrice":-1.25}  | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {"name":"   ","currentPrice":-1.25}       | 400         | BAD_REQUEST        | false   | input data validation           |
      | POST   | /api/stocks   | {"name":"GOOG","currentPrice":"  "}       | 400         | BAD_REQUEST        | false   | input data validation           |
      | PUT    | /api/stocks   | {"name":"PAYCONIQ","currentPrice":100.25} | 405         | METHOD_NOT_ALLOWED | false   | input data validation           |
      | POST   | /api          | {"name":"PAYCONIQ","currentPrice":100.25} | 404         | NOT_FOUND          | false   | input data validation           |
      | POST   | /fake/address | {}                                        | 404         | NOT_FOUND          | false   | input data validation           |
