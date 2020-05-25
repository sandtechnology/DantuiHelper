package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;
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
        System.out.println("Logging....");
        Bot bot = BotFactoryJvm.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5(), new BotConfiguration() {
            {
                setDeviceInfo(context -> SystemDeviceInfoKt.loadAsDeviceInfo(Paths.get("config", "deviceInfo.json").toFile(), context));
            }

        });
        ConfigLoader.save();
        try {
            //bot.getCoroutineContext().plus(new ErrorHandler());
            bot.login();
            DataContainer.initialize(DataContainer.BotType.Mirai);
            System.out.println("Registering Event....");
            Events.registerEvents(bot, MessageListener.getMessageListener());
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
