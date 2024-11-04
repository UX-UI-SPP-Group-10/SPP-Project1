Feature: Create, Log in, View

  Scenario: Cucumber Test 1: Create new item listing
    Given the retailer is on the create item page
    When the retailer has entered info and pressed confirm
    Then the item should be created as a new listing

  Scenario: Cucumber Test 2: Log in as retailer
    Given the app is open and user is created
    When name and password is given and log in is pressed
    Then the retailer will be logged in to the main page

  Scenario: Cucumber Test 3: Create new user
    Given the app is open on create profile
    When information is entered and create profile is pressed
    Then user is saved in database












