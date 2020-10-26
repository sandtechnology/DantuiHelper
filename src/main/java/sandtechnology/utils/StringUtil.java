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

    public static class DelimitedStringBuilder {
        private final String[] delimiter;
        private final StringBuilder stringBuilder = new StringBuilder();

        public DelimitedStringBuilder(String... delimiter) {
            this.delimiter = delimiter;
        }

        public DelimitedStringBuilder append(String str) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(delimiter[0]);
            }
            stringBuilder.append(str);
            return this;
        }

        public DelimitedStringBuilder append(int i) {
            stringBuilder.append(i);
            return this;
        }

        public DelimitedStringBuilder append(String str, int delimiterIndex) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(delimiter[delimiterIndex]);
            }
            stringBuilder.append(str);
            return this;
        }

        public String build() {
            return stringBuilder.toString();
        }
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
