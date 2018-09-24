import com.google.gson.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class Runner {
    static Logger log = LoggerFactory.getLogger(Runner.class);
    public static JDA api;
    private static String token = "";

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

    public static void main(String[] args) throws Exception {
        loadToken();

        api = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(new EventHandler())
                .buildBlocking();

        api.getPresence().setStatus(OnlineStatus.ONLINE);

        File testSuite = new File("TestSuite.json");
        if (testSuite.exists()) {
            JsonParser jsonParser = new JsonParser();
            try {
                FileReader reader = new FileReader("TestSuite.json");
                TestSuite suite = new TestSuite();

                JsonObject obj = (JsonObject) jsonParser.parse(reader);
                Set<String> keySet = obj.keySet();
                for (String key : keySet) {
                    JsonObject json = (JsonObject) obj.get(key);
                    String input = json.get("input").toString();
                    String expectedString = json.get("expectedResult").toString();
                    String actualString = json.get("actualResult").toString();

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) != '\"') {
                            builder.append(input.charAt(i));
                        }
                    }
                    input = builder.toString();

                    TestResult expected = TestResult.getTestResultFromString(expectedString);
                    TestResult actual = TestResult.getTestResultFromString(actualString);
                    TestCase currentCase = new TestCase(input, expected, actual);
                    suite.add(currentCase);
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                System.out.println(gson.toJson(obj));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            TestSuite suite = new TestSuite();
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
}