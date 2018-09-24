import com.google.gson.Gson;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

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

        TestSuite suite = new TestSuite();
        TestCase case1 = new TestCase("!ping", TestResult.PASS, TestResult.FAIL_UNEXPECTED_OUTPUT);
        TestCase case2 = new TestCase("!help", TestResult.FAIL_OTHER, TestResult.PASS);
        case1.setName("Ping");
        case2.setName("Help");

        suite.add(case1, case2); // TODO: For some reason, add will only add one of the test cases???

        JSONObject jo = suite.toJsonObject(); // Saving the suite to a JSONObject is easier than hardcoding the entire thing, so we can just load in changes instead of recompiling
        Gson gson = new Gson();

        // This is here only to test file IO, will eventually move to someplace else
        try {
            gson.toJson(jo, new FileWriter("TestSuite.json"));
            TestSuite suite2 = gson.fromJson(new FileReader("TestSuite.json"), TestSuite.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}