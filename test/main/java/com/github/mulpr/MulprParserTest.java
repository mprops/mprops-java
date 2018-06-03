package com.github.mulpr;

import java.io.StringReader;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MulprParserTest {

    @Test
    public void testParse1() {
        String key = "key";
        String value = " Line 1 \n Line 2";
        String text = "~ " + key + " \n" + value;
        Map<String, String> props = new MulprParser().parse(new StringReader(text));
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(key, props.keySet().iterator().next());
        Assert.assertEquals(value, props.values().iterator().next());
    }

    @Test
    public void testParse2() {
        String key1 = "key1";
        String value1 = " Line 1-1 \n Line 1-2";
        String key2 = "key2";
        String value2 = " Line 1-2 \n Line 2-2";

        String text = "~ " + key1 + " \n" + value1 + "\r\n~" + key2 + "\r\n" + value2;
        Map<String, String> props = new MulprParser().parse(new StringReader(text));
        Assert.assertEquals(2, props.size());
        Assert.assertEquals(value1, props.get(key1));
        Assert.assertEquals(value2, props.get(key2));
    }

    @Test
    public void testParseKey1() {
        String keyLine = "~ good key ";
        String key = new MulprParser().parseKey(keyLine, 1);
        Assert.assertEquals("good key", key);
    }

    @Test
    public void testParseKey2() {
        String keyLine = "~~ good key ";
        String key = new MulprParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key", key);
    }

    @Test
    public void testParseKey3() {
        String keyLine = "~~ good key ~";
        String key = new MulprParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key ~", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad1() {
        new MulprParser().parseKey("bad key ", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad2() {
        new MulprParser().parseKey("~ ", 1);
    }

}