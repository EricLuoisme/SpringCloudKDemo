package com.kdemo.springcloud;

import org.junit.Test;

/**
 * Must failed test, for checking Surefire plugin
 */
public class MustFailTest {

    @Test
    public void mustFail() {
        String err = null;
        System.out.println(err.substring(0, 10));
    }
}
