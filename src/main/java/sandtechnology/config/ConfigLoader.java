package sandtechnology.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import sandtechnology.config.section.LiveCheckerData;
import sandtechnology.config.section.ModuleEnablerData;
import sandtechnology.config.section.SubscribeConfig;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.JsonHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Scanner;

public class ConfigLoader {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path configPath = Paths.get("config", "config.json");
    private static ConfigHolder holder;
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();


    private ConfigLoader() {
    }

    synchronized public static ConfigHolder getHolder() {
        if (holder == null) {
            load();
        }
        return holder;
    }

    synchronized public static ConfigHolder load() {
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
            }
            holder = JsonHelper.getGsonInstance().fromJson(Files.newBufferedReader(configPath), ConfigHolder.class);
            //防止NPE
            if (holder == null) {
                holder = JsonHelper.getGsonInstance().fromJson("{}", ConfigHolder.class);
            }
            for (Field field : holder.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Ask.class) && !field.isSynthetic()) {
                    {
                        Ask askAnnotation = field.getAnnotation(Ask.class);
                        if (askAnnotation.miraiOnly() && DataContainer.getDataContainer().getBotType() != DataContainer.BotType.Mirai) {
                            continue;
                        }
                        if (field.get(holder).toString().equals(askAnnotation.defaultValue())) {
                            System.out.print(String.format(askAnnotation.text(), field.getName()));
                            Class<?> type = field.getType();

                            if (!type.isArray()) {
                                if ((type.isPrimitive() && type.getName().equals("double")) || type.isInstance(Double.class)) {
                                    field.setDouble(holder, scanner.nextDouble());
                                } else if ((type.isPrimitive() && type.getName().equals("int")) || type.isInstance(Integer.class)) {
                                    field.setInt(holder, scanner.nextInt());
                                } else if ((type.isPrimitive() && type.getName().equals("long")) || type.isInstance(Long.class)) {
                                    field.setLong(holder, scanner.nextLong());
                                } else if ((type.isPrimitive() && type.getName().equals("short")) || type.isInstance(Short.class)) {
                                    field.setShort(holder, scanner.nextShort());
                                } else if ((type.isPrimitive() && type.getName().equals("float")) || type.isInstance(Float.class)) {
                                    field.setFloat(holder, scanner.nextFloat());
                                } else if ((type.isPrimitive() && type.getName().equals("boolean")) || type.isInstance(Boolean.class)) {
                                    field.setBoolean(holder, scanner.nextBoolean());
                                } else if ((type.isPrimitive() && type.getName().equals("byte")) || type.isInstance(Byte.class)) {
                                    field.setByte(holder, scanner.nextByte());
                                } else if (type.getName().equals("java.lang.String")) {
                                    field.set(holder, scanner.next());
                                }
                            }
                            if (field.get(holder) == null) {
                                //备用
                                @SuppressWarnings("rawtypes") Class<? extends StringConverter> converter = field.getAnnotation(Ask.class).converter();
                                field.set(holder, converter.newInstance().convert(scanner.next()));
                            }
                        }
                        System.out.println(field.getName() + "=" + field.get(holder).toString());
                    }
                }

            }
            save();
            return holder;
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public static void save() {
        try {
            Files.write(configPath, Collections.singleton(gson.toJson(getHolder())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfigHolder {

        @SerializedName("QQ")
        @Ask(defaultValue = "-1", text = "请输入QQ：", miraiOnly = true)
        long qq = -1;
        @SerializedName("Password")
        @Ask(text = "请输入密码：", miraiOnly = true)
        String password = "";
        @SerializedName("subscribeConfig")
        SubscribeConfig subscribeNodeMap = new SubscribeConfig();
        @Ask(defaultValue = "-1", text = "请输入主人QQ号：")
        long master = -1;
        @SerializedName("PasswordMD5")
        byte[] passwordMD5;
        @Ask(defaultValue = "-1", text = "请输入管理群号：")
        long masterGroup = -1;
        @SerializedName("liveData")
        LiveCheckerData liveCheckerData = new LiveCheckerData();
        @SerializedName("moduleData")
        ModuleEnablerData moduleEnablerData = new ModuleEnablerData();
        @SerializedName("usingLiveNewAPI")
        boolean usingLiveNewAPI = false;

        public boolean isUsingLiveNewAPI() {
            return usingLiveNewAPI;
        }

        public LiveCheckerData getLiveCheckerData() {
            return liveCheckerData;
        }

        public ModuleEnablerData getModuleEnablerData() {
            return moduleEnablerData;
        }

        public SubscribeConfig getSubscribeNodeMap() {
            return subscribeNodeMap;
        }

        public long getMasterGroup() {
            return masterGroup;
        }

        public long getMaster() {
            return master;
        }

        public long getQQ() {
            return qq;
        }

        public byte[] getPasswordMD5() {
            if (password != null && !password.equals("[已自动转换]")) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    passwordMD5 = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    password = "[已自动转换]";
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            return passwordMD5;
        }

    }
}
