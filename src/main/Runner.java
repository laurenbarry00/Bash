/**
 * The main class in which the bot in initialized.
 * @author Lauren Barry
 * @version 1.0
 */

import com.google.gson.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Set;
import java.util.UUID;

public class Runner {
    static Logger log = LoggerFactory.getLogger(Runner.class);
    public static JDA api;
    private static String token = "";
    static TestSuite suite;

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
    static void loadJsonTestSuite() {
        File testSuite = new File("TestSuite.json");
        if (testSuite.exists() && testSuite.length() > 0) {
            JsonParser jsonParser = new JsonParser();
            try {
                FileReader reader = new FileReader("TestSuite.json");
                suite = new TestSuite();

                JsonElement element = jsonParser.parse(reader);
                JsonObject obj = element.getAsJsonObject();
                Set<String> keySet = obj.keySet();
                for (String key : keySet) {
                    JsonObject current = obj.getAsJsonObject(key);
                    UUID newUUID;
                    try {
                        String uuid = current.get("uuid").toString();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < uuid.length(); i++) {
                            if (uuid.charAt(i) != '"') {
                                builder.append(uuid.charAt(i));
                            }
                        }
                        uuid = builder.toString();
                        newUUID = UUID.fromString(uuid.trim());
                    } catch (IllegalArgumentException e) {
                        newUUID = UUID.randomUUID();
                        log.warn("Invalid UUID for " + current.get("input") + ". Program proceeding with newly generated UUID, but JSON file will still be incorrect!");
                        try {
                            FileWriter writer = new FileWriter("TestSuite.json");
                            writer.write(current.toString());
                            writer.close();
                        } catch (IOException ex) {
                            e.printStackTrace();
                        }
                    }
                    String input = current.get("input").toString();
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
                    TestCase currentCase = new TestCase(newUUID, input,  expected, actual);

                    suite.add(currentCase);
                }
                log.info("Successfully loaded " + keySet.size() + " TestCases into TestSuite.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            suite = new TestSuite();
            TestCase case1 = new TestCase(UUID.randomUUID(), "!ping", TestResult.PASS, TestResult.FAIL_UNEXPECTED_OUTPUT);
            TestCase case2 = new TestCase(UUID.randomUUID(), "!help", TestResult.FAIL_UNEXPECTED_OUTPUT, TestResult.FAIL_OTHER);
            suite.add(case1, case2);
            JSONObject jo = suite.toJsonObject(); // Saving the suite to a JSONObject is easier than hardcoding the entire thing, so we can just load in changes instead of recompiling

            try {
                FileWriter writer = new FileWriter("TestSuite.json");
                writer.write(jo.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void saveJsonTestSuite() {
        try {
            File testSuiteFile = new File("TestSuite.json");
            if (!testSuiteFile.exists()) {
                testSuiteFile.createNewFile();
            }
            JSONObject json = suite.toJsonObject();
            FileWriter writer = new FileWriter(testSuiteFile);
            writer.write(json.toString());
            writer.close();

        } catch (IOException e) {
            log.error("Could not save TestSuite to JSON file!");
            e.printStackTrace();
        }
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


    }
}