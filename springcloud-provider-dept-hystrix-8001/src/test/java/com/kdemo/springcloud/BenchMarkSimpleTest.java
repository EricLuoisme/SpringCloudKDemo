package com.kdemo.springcloud;

import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

/**
 * 简单测试Benchmark注解
 *
 * @author Roylic
 * 2022/5/17
 */
public class BenchMarkSimpleTest {

    @Benchmark
    public static String testBenchMark() {
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        return String.valueOf(sum);
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }


}
