package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;
import sandtechnology.common.MessageListener;
import sandtechnology.common.Start;
import sandtechnology.config.ConfigLoader;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.ThreadHelper;

import java.nio.file.Paths;


public class Mirai {


    public static Bot getBot() {
        return Bot.getInstance(ConfigLoader.getHolder().getQQ());
    }

    public static void main(String[] args) {

        System.out.println("Welcome to Love-TokimoriSeisa-Forever system! (Mirai version)");
        DataContainer.initialize(DataContainer.BotType.Mirai);
        System.out.println("Logging....");
        Bot bot = BotFactoryJvm.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5(), new BotConfiguration() {
            {
                fileBasedDeviceInfo(Paths.get("config", "deviceInfo.json").toAbsolutePath().toString());
            }
        });
        ConfigLoader.save();
        try {
            bot.login();
            System.out.println("Registering Event....");
            Events.registerEvents(bot, MessageListener.getMessageListener());
            //加载动态轮询器
            Start.start();
            System.out.println("Done!");
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> DataContainer.getMessageHelper().sendingErrorMessage(e, t.getName() + "线程发生了异常："));
            bot.join();
        } catch (Throwable e) {
            bot.close(e);
            Start.exit();
            e.printStackTrace();
            ThreadHelper.sleep(3000);
            main(args);
        }

    }
}
