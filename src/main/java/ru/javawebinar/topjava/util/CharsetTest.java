package ru.javawebinar.topjava.util;

import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;

import static org.slf4j.LoggerFactory.getLogger;

public class CharsetTest {

    private static final Logger log = getLogger(CharsetTest.class);

    public static void main(String[] args) {
        String s = "лалала";
        log.debug(s);
        System.out.println("sout s: = " + s);
        try {
            System.out.print("charset s: ");
            System.out.println(new String(s.getBytes("cp1251"), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
