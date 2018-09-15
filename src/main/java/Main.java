import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        String token = "-1";
        try {
            final File tokenFile = new File("token.txt");
            BufferedReader br = new BufferedReader(new FileReader(tokenFile));
            token = br.readLine();
            br.close();
        } catch (IOException e) {
           System.out.println("Error loading token from file:");
           e.printStackTrace();
           System.exit(1);
        }

        JDA api  = new JDABuilder(token).build();
    }
}
