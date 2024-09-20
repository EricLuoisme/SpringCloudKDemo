package com.kdemo.springcloud.score.convertor;

import com.kdemo.springcloud.score.ScoreConvertor;

import java.time.Instant;

/**
 * First in rank first convertor
 */
public class FIRFConvertor implements ScoreConvertor {

    private static final long BIG_NUM = Integer.MAX_VALUE * 10L;

    private static final int REMAIN_DIGIT = 11;


    @Override
    public Double convertToZSetScore(Integer externalScore) {

        // fix big num - current timestamp
        long curTimestampInSec = Instant.now().toEpochMilli() / 1000;
        long decreaseNum = BIG_NUM - curTimestampInSec;

        // use string act as divide, add 0 to front
        StringBuilder sb = new StringBuilder(String.valueOf(decreaseNum));
        while (sb.length() < REMAIN_DIGIT) {
            sb.insert(0, "0");
        }

        // externalScore.decimalPart
        double decimalPart = Double.parseDouble("0." + sb);
        return externalScore + decimalPart;
    }

    @Override
    public Integer convertFromZSetScore(Double cacheScore) {
        // just cut the decimal part
        String[] split = cacheScore.toString().split("\\.");
        return Integer.parseInt(split[0]);
    }
}
