# Bash
## A Discord bot to test ~~all~~ Discord bots!

#### Description
This Discord bot was created with the intention of using it to automate unit tests for [AvaIre](https://avairebot.com/). This bot was created as a project for CS-320, Quality Assurance in Software Development, taught by Pete Tucker. Its purpose is to automate interaction with Alvare in order to perform various tests of the quality of the software. 

### Process
AvaIre is an open-source Discord bot that is used on over 25,000 Discord servers. In order to run our automated tests, we needed to self-host (that is, make our own copy of the bot, instead of the publically available version) so that it could respond to our own bot (Bash). This quickly became more difficult, as AvaIre requires an SQL database in order to function properly. So, I created an AWS RDS instance to run a MySQL database, and an EC2 instance to run the bot on - just 'cause. 
