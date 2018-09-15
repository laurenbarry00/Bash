import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Runner {
    private static  String token = "";

    private static void loadToken() throws Exception {
        try {
            File file = new File("token.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            token = br.readLine(); // Can't hardcode the bot token due to security, so have to load it from a file

            JDA api = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking(); // Creating an instance of the bot & logging in
        } catch (IOException e) {
            System.out.println("Error loading token from file!");
            e.printStackTrace();
        } catch (LoginException e) {
            System.out.println("Error logging in with token.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        loadToken();
    }
}
