Feature: IQOption Rest API test

  Background:
    And Web service is disconnected

  Scenario Outline: Web service returns 3000 pairs if time-and-quotation, where quotation less than 2
    When Web service is connected
    And Request is executed
    Then Response object have following parameters:
      | isSuccessful | message | result |
      | true         | []      |        |
    And Parameter result have following parameters with values:
      | graph | percent | active_name  |
      |       |         | front.EURUSD |
    And Parameter graph have <elementsSize> elements with <valuesInElementSize> and second value on each element is less than <quotation>
    And Parameter percent is in the range of 0..100 inclusively

    Examples: searching by "active_id=1"
      | elementsSize | valuesInElementSize | quotation |
      | 3000         | 2                   | 2         |
