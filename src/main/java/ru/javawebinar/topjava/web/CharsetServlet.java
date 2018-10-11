package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import static org.slf4j.LoggerFactory.getLogger;

public class CharsetServlet extends HttpServlet {
    private static final Logger log = getLogger(CharsetServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();


        try {
            Field cs = Charset.class.getDeclaredField("defaultCharset");
            cs.setAccessible(true);
            cs.set(null, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String s = "лалала";
        log.debug(s);
        System.out.println("sout s: = " + s);
        try {
            System.out.print("charset s: ");
            System.out.println(new String(s.getBytes("cp1251"), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("defaultCharset: "+Charset.defaultCharset());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
