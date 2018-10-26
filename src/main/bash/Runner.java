package bash; /**
 * The bash class in which the bot in initialized.
 * @author Lauren Barry
 */

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import commands.Command;
import commands.RunAllTestsCommand;
import commands.ShutdownCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.TestCase;
import test.TestResult;
import test.TestSuite;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Runner {
    static final Logger log = LoggerFactory.getLogger(Runner.class);
    private static List<Command> commandsList;
    private static JDA api;
    private static String token = "";
    private static List<TestSuite> suiteList = new ArrayList<>();


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
            System.exit(-1);
        }
    }

    /**
     * Creates or loads a TestSuite from JSON.
     */
    private static void loadJsonTestSuite() {
        File folder = new File("C:\\Users\\flame\\Coding\\Bash\\tests");
        File[] fileList = folder.listFiles();

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

                    String commandString = suite.get(0).getInput();
                    int prefixIndex = 0, endCommandIndex = 0;
                    for (int i = 0; i < commandString.length(); i++) {
                        if (commandString.charAt(i) == '!') {
                            prefixIndex = i;
                            for (int j = 0; j < commandString.length(); j++) {
                                if (commandString.charAt(j) == ' ') {
                                    endCommandIndex = j - 1;
                                }
                            }
                        }
                    }
                    String name = commandString.substring(prefixIndex, endCommandIndex);
                    suite.setName(name);

                    log.info("Successfully loaded " + keySet.size() + " TestCases into TestSuite. (From file " + testSuite.getName() + ")");
                    suiteList.add(suite);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("Successfully loaded " + suiteList.size() + " TestSuites.");
    }

    private static void createAndFillCommandsList() {
        commandsList = new ArrayList();
        commandsList.add(new RunAllTestsCommand());
        commandsList.add(new ShutdownCommand());
    }

    public static List<Command> getCommandsList() {
        return commandsList;
    }

    public static List<TestSuite> getTestSuiteList() {
        return suiteList;
    }

    public static JDA getApi() {
        return api;
    }

    public static Logger getLogger() {
        return log;
    }

    public static void main(String[] args) {
        loadToken();

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