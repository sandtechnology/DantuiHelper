package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.Nullable;
import sandtechnology.common.MessageListener;
import sandtechnology.common.Start;
import sandtechnology.config.ConfigLoader;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.ThreadHelper;

import java.io.PrintStream;
import java.nio.file.Paths;


public class Mirai {


    public static Bot getBot() {
        return Bot.getInstance(ConfigLoader.getHolder().getQQ());
    }

    private static final MiraiLogger logger = MiraiLogger.create("Mirai");

    public static void main(String[] args) {

        logger.info("Welcome to Love-TokimoriSeisa-Forever system! (Mirai version)");
        DataContainer.initialize(DataContainer.BotType.Mirai);
        logger.info("Logging....");
        //接管Println
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(@Nullable String x) {
                logger.info(x);
            }
        });
        Bot bot = BotFactory.INSTANCE.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5(), new BotConfiguration() {
            {
                fileBasedDeviceInfo(Paths.get("config", "deviceInfo.json").toAbsolutePath().toString());
                setBotLoggerSupplier(bot -> logger);
            }
        });
        ConfigLoader.save();
        try {
            bot.login();
            logger.info("Registering Event....");
            bot.getEventChannel().registerListenerHost(MessageListener.getMessageListener());
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

    public MiraiLogger getLogger() {
        return logger;
    }
}
