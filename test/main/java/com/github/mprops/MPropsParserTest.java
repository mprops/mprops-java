package com.github.mprops;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MPropsParserTest {

    @Test
    public void testParse1() {
        String key = "key";
        String value = " Line 1 \n Line 2";
        String text = "~ " + key + " \n" + value;
        Map<String, String> props = new MPropsParser().parse(text);
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(key, props.keySet().iterator().next());
        Assert.assertEquals(value, props.values().iterator().next());
    }

    @Test
    public void testParse2() {
        String key1 = "key1";
        String value1 = " Line 1-1 \n Line 1-2";
        String key2 = "key2";
        String value2 = " Line 1-2 \nLine 2-2";

        String text = "~ " + key1 + " \n" + value1 + "\r\n~" + key2 + "\r\n" + value2;
        Map<String, String> props = new MPropsParser().parse(text);
        Assert.assertEquals(2, props.size());
        Assert.assertEquals(value1, props.get(key1));
        Assert.assertEquals(value2, props.get(key2));
    }

    @Test
    public void testParseEmptyValue() {
        String key1 = "key1";
        String key2 = "key2";
        Map<String, String> result = new MPropsParser().parse("~" + key1 + "\n~" + key2 + "\n");
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("", result.get(key1));
        Assert.assertEquals("", result.get(key2));
    }

    @Test
    public void testParseEmptyValueNoNewLine() {
        String key = "key";
        Map<String, String> result = new MPropsParser().parse("~" + key);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("", result.get(key));
    }

    @Test(expected = RuntimeException.class)
    public void testParseOnIOException() {
        new MPropsParser().parse(new StringReader("never parsed") {
            @Override
            public int read() throws IOException {
                throw new IOException("error1");
            }

            @Override
            public int read(@NotNull char[] buf, int off, int len) throws IOException {
                throw new IOException("error2");
            }
        });
    }

    @Test
    public void testParseWithHeaderComment() {
        String key = "key";
        String value = "Line 1-1 \n Line 1-2";

        String text = "here is a header comment\n with multiple lines and \n ~ inside\n\n.\n~ " + key + " \n" + value;
        Map<String, String> props = new MPropsParser().parse(text);
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(value, props.get(key));
    }

    @Test
    public void testParseEscapedValue1() {
        String key = "key";
        String value = "~value";

        String text = "~" + key + "\n " + value;
        Map<String, String> props = new MPropsParser().parse(text);
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(value, props.get(key));
    }

    @Test
    public void testParseEscapedValue2() {
        String key = "key";
        String value = " ~value";

        String text = "~" + key + "\n " + value;
        Map<String, String> props = new MPropsParser().parse(text);
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(value, props.get(key));
    }

    @Test
    public void testParseKey1() {
        String keyLine = "~ good key ";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("good key", key);
    }

    @Test
    public void testParseKey2() {
        String keyLine = "~~ good key ";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyWrongContext1() {
        new MPropsParser().parseKey(" ~key", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyWrongContext2() {
        new MPropsParser().parseKey("", 1);
    }

    @Test
    public void testParseKey3() {
        String keyLine = "~~ good key ~";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key ~", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad1() {
        new MPropsParser().parseKey("bad key ", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad2() {
        new MPropsParser().parseKey("~ ", 1);
    }


    @Test
    public void testBiConsumer() {
        List<String> keys = new ArrayList<>();
        new MPropsParser().parse(new StringReader("~key\nvalue1\n~key\nvalue2"), (key, value) -> keys.add(key));
        Assert.assertEquals(2, keys.size());
        Assert.assertEquals("key", keys.get(0));
        Assert.assertEquals("key", keys.get(1));
    }
}