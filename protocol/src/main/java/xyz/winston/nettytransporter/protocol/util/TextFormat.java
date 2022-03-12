package xyz.winston.nettytransporter.protocol.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author winston
 */
public final class TextFormat {

    private final String[] textParts;
    private final String empty;

    public TextFormat(String text) {
        List<String> textParts = new LinkedList<>();
        int start = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{' && text.charAt(i + 1) == '}') {
                textParts.add(text.substring(start, i));

                i++;
                start = i + 1;
            }
        }

        textParts.add(text.substring(start));

        // К удивлению, LinkedList
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        this.textParts = textParts.toArray(new String[textParts.size()]);
        this.empty = String.join("", text);
    }

    public String format(Object... o) {
        if (o == null || o.length == 0) return format();

        StringBuilder sb = FastStrings.getEmptyBuilder();

        for (int i = 0; i < textParts.length; i++) {
            sb.append(textParts[i]);

            if (i < o.length)
                sb.append(o[i]);
        }

        return sb.toString();
    }

    public String format() {
        return empty;
    }

    public static String formatText(String text, Object... o) {
        if (o.length == 0) {
            return text;
        }

        int idx = 0;

        StringBuilder sb = FastStrings.getEmptyBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{' && text.charAt(i + 1) == '}') {
                sb.append(idx >= o.length ? "" : o[idx++]);
                i++;

                continue;
            }

            sb.append(ch);
        }

        return sb.toString();
    }

}
