package sandtechnology.utils;


import org.bouncycastle.util.Arrays;

public class StringUtil {
    private StringUtil() {
    }


    public static String delete(String s, char... chars) {
        char[] originString = s.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(originString.length);
        for (char c : originString) {
            if (!Arrays.contains(chars, c)) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.length() == s.length() ? s : stringBuilder.toString();
    }
}
