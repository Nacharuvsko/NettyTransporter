package xyz.winston.nettytransporter.protocol.util;

/**
 * @author winston
 */
public final class ParseUtils {

    private ParseUtils() {
        throw new UnsupportedOperationException();
    }

    public static long parseLong(CharSequence sequence) {
        return parseLong(sequence, 0, sequence.length());
    }

    public static long parseLong(CharSequence sequence, int start, int end) {
        boolean negate = false;
        long result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c < '0' || c > '9') {
                continue;
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static int parseInt(CharSequence sequence) {
        return parseInt(sequence, 0, sequence.length());
    }

    public static int parseInt(CharSequence sequence, int start, int end) {
        boolean negate = false;
        int result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c < '0' || c > '9') {
                continue;
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static double parseDouble(CharSequence sequence) {
        return parseDouble(sequence, 0, sequence.length());
    }

    public static double parseDouble(CharSequence sequence, int start, int end) {
        boolean negate = false;

        int x = 0;
        int y = 0;
        long yDivision = 1;

        boolean dot = false;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == '.') {
                dot = true;
                continue;
            }

            if (c < '0' || c > '9') {
                continue;
            }

            int v = c & 0xF;

            if (dot) {
                y = y * 10 + v;
                yDivision *= 10;
            } else {
                x = x * 10 + v;
            }
        }

        double result = x + y / (double) yDivision;

        return negate ? -result : result;
    }

    public static float parseFloat(CharSequence sequence) {
        return parseFloat(sequence, 0, sequence.length());
    }

    public static float parseFloat(CharSequence sequence, int start, int end) {
        boolean negate = false;

        int x = 0;
        int y = 0;
        int yDivision = 1;

        boolean dot = false;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == '.') {
                dot = true;
                continue;
            }

            if (c < '0' || c > '9') {
                continue;
            }

            int v = c & 0xF;

            if (dot) {
                y = y * 10 + v;
                yDivision *= 10;
            } else {
                x = x * 10 + v;
            }
        }

        float result = x + y / (float) yDivision;

        return negate ? -result : result;
    }

}
