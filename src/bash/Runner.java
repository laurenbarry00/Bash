package bash;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.TestCase;
import test.TestResult;
import test.TestSuite;
import commands.*;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Bash class in which the bot in initialized.
 * @author Lauren Barry
 */

public class Runner {
    static final Logger log = LoggerFactory.getLogger(Runner.class);
    private static List<commands.Command> commandsList;
    private static JDA api;
    private static String token = "";
    private static List<TestSuite> suiteList = new ArrayList<>();

    private static boolean isAutoPurge = false; // Automatically delete message spam after executing tests?


    /**
     * Loads Bot token from file. Token is PRIVATE, not on source control.
     */
    private static void loadToken() {
        try {
            File file = new File("token.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            token = br.readLine(); // Can't hardcode the bot token due to security, so have to load it from a file
        } catch (IOException e) {
            System.err.println("Error loading token from file!\nAborting login.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Creates or loads a TestSuite from JSON.
     */
    private static void loadJsonTestSuite() {
        File folder = new File("C:\\Users\\flame\\Coding\\Bash\\tests");
        File[] fileList = folder.listFiles();

        // Each TestSuite is stored in a JSON file
        for (File testSuite : fileList) {
            if (testSuite.exists() && testSuite.length() > 0) {
                JsonParser jsonParser = new JsonParser();
                try {
                    FileReader r = new FileReader(testSuite);
                    JsonReader reader = new JsonReader(r);
                    reader.setLenient(true);
                    TestSuite suite = new TestSuite();

                    JsonElement element = jsonParser.parse(reader);
                    JsonObject obj = element.getAsJsonObject();
                    Set<String> keySet = obj.keySet();
                    for (String key : keySet) {
                        JsonObject current = obj.getAsJsonObject(key);
                        String input = current.get("input").toString();
                        String output = current.get("output").toString();
                        String expectedString = current.get("expectedResult").toString();
                        String actualString = current.get("actualResult").toString();

                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < input.length(); i++) {
                            if (input.charAt(i) != '\"' && input.charAt(i) != '"') {
                                builder.append(input.charAt(i));
                            }
                        }
                        input = builder.toString();

                        TestResult expected = TestResult.getTestResultFromString(expectedString);
                        TestResult actual = TestResult.getTestResultFromString(actualString);
                        TestCase currentCase = new TestCase(key, input, output, expected, actual);

                        suite.add(currentCase);
                    }

                    suite.setName(testSuite.getName());

                    log.info("Successfully loaded " + keySet.size() + " TestCases into TestSuite. (From file " + testSuite.getName() + ")");
                    suiteList.add(suite);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("Successfully loaded " + suiteList.size() + " TestSuites.");
    }

    /**
     * Creates the list of commands to be accessed by EventHandler.onMessageReceived()
     */
    private static void createAndFillCommandsList() {
        commandsList = new ArrayList<>();
        commandsList.add(new RunAllTestsCommand());
        commandsList.add(new ShutdownCommand());
        commandsList.add(new PurgeCommand());
        commandsList.add(new ListAllTestsCommand());
        commandsList.add(new RunTestCommand());
    }

    /**
     * Retrieves the List of commands
     * @return commandsList
     */
    public static List<Command> getCommandsList() {
        return commandsList;
    }

    /**
     * Returns the list of loaded Test Suites
     * @return suiteList
     */
    public static List<TestSuite> getTestSuiteList() {
        return suiteList;
    }

    /**
     * Returns the JDA instance.
     * @return api
     */
    public static JDA getApi() {
        return api;
    }

    /**
     * Returns the Logger instance.
     * @return log
     */
    public static Logger getLogger() {
        return log;
    }

    public static boolean isAutoPurge() {
        return isAutoPurge;
    }

    public static void main(String[] args) {
        loadToken(); // Loads bot token from file

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-purge")) {
                isAutoPurge = true;
                log.info("Auto Purge enabled.");
            }
        }

        try {
            api = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new EventHandler())
                    .buildBlocking();

            api.getPresence().setStatus(OnlineStatus.ONLINE);
        } catch (LoginException e) {
            log.error("Error upon logging in: \n");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        loadJsonTestSuite();
        createAndFillCommandsList();
    }
}
