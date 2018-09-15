public class Main {
    public static void main(String[] args) throws Exception {
        String token = "-1";
        try {
            // final File tokenFile = new File("token.txt");
            // BufferedReader br = new BufferedReader(new FileReader(tokenFile));
            // token = br.readLine();
            // br.close();
            System.out.println("Success"); 
        } catch (IOException e) {
           System.out.println("Error loading token from file:");
           e.printStackTrace();
           System.exit(1);
        }

        JDA api  = new JDABuilder(token).build();

    }
}
