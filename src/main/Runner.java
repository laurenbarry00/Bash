import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Runner {
    Logger log = LoggerFactory.getLogger(Runner.class);
    private static  String token = "";

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

        JDA api = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking(); // Creating an instance of the bot & logging in
        api.getPresence().setStatus(OnlineStatus.ONLINE);

        api.addEventListener(new CommandHandler());
    }
}
