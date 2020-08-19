package sandtechnology.utils;


public class StringUtil {
    private StringUtil() {
    }

    private static boolean contains(char[] array, char value) {
        for (char v : array) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

    public static String delete(String s, char... chars) {
        char[] originString = s.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(originString.length);
        for (char c : originString) {
            if (!contains(chars, c)) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.length() == s.length() ? s : stringBuilder.toString();
    }
}
