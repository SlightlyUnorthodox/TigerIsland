Feature: System Setup

  Scenario: Every game has a complete set of rules
    Given a game is created
    And that game has players
    Then then the game has rules the following moves:
      | TilePlacement |
      | VillageCreation |
      | VillageExpansion |
      | TotoroPlacement |