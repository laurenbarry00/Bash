# Bash
*A Discord bot to test a Discord bot!*

Developed By: Lauren Barry

## Description
Bash is a Discord bot. It was created in order to automate tests for another Discord bot, [AvaIre](https://avairebot.com/). It is part of a project for the Quality Assurance in Software Development class at Whitworth University, instructed by Pete Tucker. 

## Process
AvaIre is an open-source Discord bot that is used on over 25,000 Discord servers. In order to run our automated tests, we needed to self-host (that is, make our own copy of the bot, instead of the publically available version) so that it could respond to our own bot (Bash). This quickly became more difficult, as AvaIre requires an SQL database in order to function properly. So, I created an AWS RDS instance to run a MySQL database. From there I further developed Bash to load Test Cases from various JSON files, execute them, and evaluate their results. Bash also includes a few quality of life commands that perform functions such as mass-deleting spammy messages and running tests.

## Commands
**?runalltests**
> Iterates through all loaded TestSuites and runs all Test Cases.

**?listalltests**
> Lists all available Test Suites.

**?runtest \<test\>**
> Runs a specific Test Suite. *Example: ?runtest !calc*

**?purge**
> Clears all messages in the #testing channel.
*Note: This can be slow due to rate-limiting.*
