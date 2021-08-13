Feature: Rest Web Service to fetch detail of specific stock with identifier

  Background: Assumption Stocks List
    Given assume following stocks
      | 11 | PAYCONIQ | 100.25 |
      | 12 | AAPL     | 200.25 |
      | 31 | AMZN     | 300.25 |
      | 41 | GOOG     | 400.25 |

  Scenario Outline: Client call web service to fetch detail of stock by passing it's identifier
    When call service with <url>
    Then response status code is <response-status-code>
    And  response stock's name is <response-stock-name>

    Examples:
      | url            | response-status-code | response-stock-name |
      | /api/stocks/11 | 200                  | PAYCONIQ            |
      | /api/stocks/31 | 200                  | AMZN                |
      | /api/stocks/0  | 404                  | ---                 |
      | /api/stocks/-1 | 404                  | ---                 |
      | /api/stocks/10 | 404                  | ---                 |
      | /api/stocks/a  | 400                  | ---                 |

