package sandtechnology;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import sandtechnology.config.ConfigLoader;


public class Mirai {
    public static void main(String[] args) {
        try {
            System.out.println("Welcome to Love-TokimoriSeisa-Forever system! (Mirai version)");
            System.out.println("Logging....");
            Bot bot = BotFactoryJvm.newBot(ConfigLoader.getHolder().getQQ(), ConfigLoader.getHolder().getPasswordMD5());
            bot.login();
            System.out.println("Registering Event....");
        } catch (Throwable e) {
            ConfigLoader.save();
        }

    }
}
