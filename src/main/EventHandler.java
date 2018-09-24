import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;


public class EventHandler extends ListenerAdapter {

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        User author =  event.getAuthor();
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();

        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();

        } else if (event.isFromType(ChannelType.PRIVATE)) {
            PrivateChannel privateChannel = event.getPrivateChannel();
        }

        String messageContent = msg.getContentDisplay();
    }

    @Override
    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        Runner.log.info("Bash logged into " + event.getJDA().getGuilds().size() + " Discord server(s) upon startup.");
    }
}
