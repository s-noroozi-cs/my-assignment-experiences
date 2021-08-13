Feature: Rest Web Service to fetch stock list with sort feature and pageable facility

  Background: Assumption Stocks List
    Given We have following stocks
      | PAYCONIQ | 100.25 |
      | AAPL     | 200.25 |
      | AMZN     | 300.25 |
      | GOOG     | 400.25 |

  Scenario: client fetch all stocks
    When the client calls GET /api/stocks
    Then the client receives status code of 200
    And  the client receives 4 stocks

  Scenario: client fetch only three highest price of stocks
    When then client call with page size and sort by GET /api/stocks?page=0&size=3&sort=currentPrice,DESC
    Then the client receives status code of 200
    And  the client receives "GOOG" and "AMZN" and "AAPL"

  Scenario: client fetch lowest price stock
    When then client call only one page with size 1 that sort price trough GET /api/stocks?page=0&size=1&sort=currentPrice,ASC
    Then the client receives status code of 200
    And  the client receives "PAYCONIQ"