package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.japt.Events;
import net.mamoe.mirai.message.FriendMessage;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;
import sandtechnology.common.Listener;
import sandtechnology.config.ConfigLoader;

import java.nio.file.Paths;


public class Mirai {
    public static void main(String[] args) {

            System.out.println("Welcome to Love-TokimoriSeisa-Forever system! (Mirai version)");
            System.out.println("Logging....");
        Bot bot = BotFactoryJvm.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5(), new BotConfiguration() {
            {
                setDeviceInfo(context -> SystemDeviceInfoKt.loadAsDeviceInfo(Paths.get("config", "deviceInfo.json").toFile(), context)
                );
            }
        });
        try {
            bot.login();
            System.out.println("Registering Event....");
            Events.subscribeAlways(GroupMessage.class, groupMessage -> Listener.onGroupMsg(groupMessage.getSender().getId(), groupMessage.getGroup().getId(), groupMessage.getMessage().toString()));
            Events.subscribeAlways(FriendMessage.class, friendMessage -> Listener.onPrivateMsg(friendMessage.getSender().getId(), friendMessage.getMessage().toString()));
            bot.join();
        } catch (Throwable e) {
            ConfigLoader.save();
            bot.close(e);
            main(args);
        }

    }
}
