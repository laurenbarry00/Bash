# Bash
### A Discord bot to test a Discord bot!

Developed By: Lauren Barry

[JavaDocs](https://laurenbarry00.github.io/bash-docs/index.html)

[Bash Website](https://laurenbarry00.github.io/bash.html)

## Description
Bash is a Discord bot. It was created in order to automate tests for another Discord bot, [AvaIre](https://avairebot.com/). It is part of a project for the Quality Assurance in Software Development class at Whitworth University, instructed by Pete Tucker. 

## Commands
**?runalltests**
> Iterates through all loaded TestSuites and runs all Test Cases.

**?listalltests**
> Lists all available Test Suites.

**?runtest \<suite\>**
> Runs a specific Test Suite. *Example: ?runtest Calc.json*

**?purge**
> Clears all messages in the #testing channel.
*Note: This can be slow due to rate-limiting.*

## Build/Run
Bash now has a small Batch script and an executable JAR in order to run. However, the bot token still needs to be loaded from a file, so this does *not* mean that Bash is public.

**Alternatively, you may use the following commands:**

`java -jar Bash.jar`
> Starts Bash normally.

`java -jar Bash.jar -purge`
> Starts Bash with auto-purging enabled (test message spam is automatically deleted, leaving only the test suite report)
