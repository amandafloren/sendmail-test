@MAIL
Feature: Send a mail

  @Mail01
  Scenario Outline: User can successfully send an email
    Given I want to send email: email <email>, password <password>, recipient <recipient>, subject <subject>, body <body>
    Then I receive the auto reply mail: email <email>, password <password>, path <path>, recipient <recipient>, key <key>
    
    Examples:
    |          email          |            password             |           recipient           |      subject   |           body          |         path      |                key               |
    | amandafloren11@gmail.com|        cqrjiysnnttfxere         | stage3.2@mailer.powerfront.com|  Test Message  |  This is a test message | /mail/u/0/#inbox  | FMfcgzGqRZjdsNfWkSgxrFxzCjPwzQbV |
    
    
    