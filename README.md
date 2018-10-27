# Bash
*A Discord bot to test a Discord bot!*
Developed By: Lauren Barry

## Description
This Discord bot was created with the intention of using it to automate unit tests for [AvaIre](https://avairebot.com/). This bot was created as a project for CS-320, Quality Assurance in Software Development, taught by Pete Tucker. Its purpose is to automate interaction with Alvare in order to perform various tests of the quality of the software. 

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
