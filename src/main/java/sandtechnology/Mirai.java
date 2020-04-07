package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.japt.Events;
import net.mamoe.mirai.message.FriendMessage;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;
import sandtechnology.common.Listener;
import sandtechnology.common.Start;
import sandtechnology.config.ConfigLoader;
import sandtechnology.holder.ReadOnlyMessage;

import java.nio.file.Paths;


public class Mirai {

    private static Bot bot;

    public static Bot getBot() {
        return bot;
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Welcome to Love-TokimoriSeisa-Forever system! (Mirai version)");
        System.out.println("Logging....");
        bot = BotFactoryJvm.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5(), new BotConfiguration() {
            {
                setDeviceInfo(context -> SystemDeviceInfoKt.loadAsDeviceInfo(Paths.get("config", "deviceInfo.json").toFile(), context));

            }

        });
        try {
            //bot.getCoroutineContext().plus(new ErrorHandler());
            bot.login();
            System.out.println("Registering Event....");
            Events.subscribeAlways(GroupMessage.class, groupMessage -> Listener.onGroupMsg(groupMessage.getSender().getId(), groupMessage.getGroup().getId(), new ReadOnlyMessage(groupMessage.getMessage())));
            Events.subscribeAlways(FriendMessage.class, friendMessage -> Listener.onPrivateMsg(friendMessage.getSender().getId(), new ReadOnlyMessage(friendMessage.getMessage())));
            Start.start();
            bot.join();
            //尚在测试阶段暂不发布
        } catch (Throwable e) {
            ConfigLoader.save();
            bot.close(e);
            bot = null;
            Start.exit();
            Thread.sleep(30000);
            main(args);
        }

    }
}