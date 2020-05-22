package sandtechnology.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.JsonHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConfigLoader {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path configPath = Paths.get("config", "config.json");
    private static ConfigHolder holder;
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private ConfigLoader() {
    }

    public static ConfigHolder getHolder() {
        if (holder == null) {
            load();
        }
        return holder;
    }

    public static ConfigHolder load() {
        try {
            if (Files.exists(configPath)) {
                holder = JsonHelper.getGsonInstance().fromJson(Files.newBufferedReader(configPath), ConfigHolder.class);
            } else {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                holder = JsonHelper.getGsonInstance().fromJson("{}", ConfigHolder.class);
            }
            for (Field field : holder.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Ask.class) && !field.isSynthetic()) {
                    {
                        if (field.get(holder).toString().equals(field.getAnnotation(Ask.class).defaultValue())) {
                            System.out.print(String.format(field.getAnnotation(Ask.class).text(), field.getName()));
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
                                Class<? extends StringConverter> converter = field.getAnnotation(Ask.class).converter();
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

    public static void save() {
        try {
            Files.write(configPath, Collections.singleton(gson.toJson(getHolder())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfigHolder {

        @SerializedName("QQ")
        @Ask(defaultValue = "-1", text = "请输入QQ：")
        long qq = -1;
        @SerializedName("Password")
        @Ask(text = "请输入密码：")
        String password = "";
        @SerializedName("PasswordMD5")
        byte[] passwordMD5;

        List<Long> targetLiveRooms = new ArrayList<>();
        List<Long> targetUsers = new ArrayList<>();

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
