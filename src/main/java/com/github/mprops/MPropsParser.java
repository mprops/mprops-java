package com.github.mprops;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Parser implementation for multiline properties (MProps) format.
 */
public class MPropsParser {

    public static final char KEY_PREFIX_CHAR = '~';

    /**
     * Parses multiline properties from the given text.
     * Throws runtime exception if parsing error occurs.
     *
     * @param text text to parse.
     * @return map of properties: [property name] : property value. Never returns null.
     */
    @NotNull
    public Map<String, String> parse(@NotNull String text) {
        return parse(new StringReader(text));
    }

    /**
     * Parses multiline properties from the given input.
     * Throws runtime exception if parsing or IO error occurs.
     * <p>
     * Closes the reader.
     *
     * @param reader an input to read.
     * @return map of properties: [property name] : property value. Never returns null.
     */
    @NotNull
    public Map<String, String> parse(@NotNull Reader reader) {
        Map<String, String> result = new HashMap<>();
        parse(reader, result::put);
        return result;
    }

    /**
     * Parses multiline properties from the given input.
     * Throws runtime exception if parsing or IO error occurs.
     * <p>
     * Closes the reader.
     *
     * @param reader             an input to read.
     * @param propertiesConsumer consumer for key/values read.
     */
    public void parse(@NotNull Reader reader, @NotNull BiConsumer<String, String> propertiesConsumer) {
        try {
            parseImpl(reader, propertiesConsumer);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read input", e);
        }
    }

    private void parseImpl(@NotNull Reader reader, @NotNull BiConsumer<String, String> consumer) throws IOException {
        String key = "";
        StringBuilder value = new StringBuilder();
        boolean readingHeader = true;
        try (BufferedReader lineReader = new BufferedReader(reader)) {
            int lineNumber = 0;
            while (true) {
                String line = lineReader.readLine();
                if (line == null) { // EOF
                    break;
                }
                lineNumber++;
                if (readingHeader && (line.isEmpty() || line.charAt(0) != KEY_PREFIX_CHAR)) {
                    continue; // Skip header's comment
                }
                if (line.charAt(0) == KEY_PREFIX_CHAR) {
                    if (!key.isEmpty()) { // put finished property to the result, start a new one
                        consumer.accept(key, value.toString());
                        value.setLength(0);
                    }
                    readingHeader = false;
                    key = parseKey(line, lineNumber);
                } else {
                    if (value.length() > 0) {
                        value.append("\n");
                    }
                    value.append(fixLinePrefix(line));
                }
            }
            if (!key.isEmpty()) {
                consumer.accept(key, value.toString());
            }
        }
    }

    @NotNull
    String fixLinePrefix(@NotNull String line) {
        if (line.isEmpty() || line.charAt(0) != ' ') {
            return line;
        }
        for (int i = 1; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != ' ') {
                if (c == KEY_PREFIX_CHAR) {
                    return line.substring(1);
                }
                break;
            }
        }
        return line;
    }

    @NotNull
    String parseKey(@NotNull String line, int lineNumber) {
        if (line.isEmpty() || line.charAt(0) != KEY_PREFIX_CHAR) {
            throw new IllegalArgumentException("Expected token name at line: " + lineNumber + ", got: " + (line.length() < 50 ? line : line.substring(0, 50)));
        }
        String key = line.substring(1).trim();
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Empty key value at line: " + lineNumber);
        }
        return key;
    }
}
