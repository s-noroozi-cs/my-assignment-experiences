Feature: Rest Web Service to delete specific stock with identifier

  Scenario Outline: Client call web service to delete specific stock by passing it's identifier
    Given I have the following stocks in system for deletion
      | 1 | GOOG | 200.75 |

    When  to delete existing stock call service with method: <method> url: <url>
    Then  after delete, status code of response is <status-code>
    And   stock with <stock-id> should not exist any more

    Examples:
      | method | url           | stock-id | status-code |
      | DELETE | /api/stocks/1 | 1        | 204         |
      | DELETE | /api/stocks/2 | 2        | 404         |
      | DELETE | /api/stocks/w | 0        | 400         |
