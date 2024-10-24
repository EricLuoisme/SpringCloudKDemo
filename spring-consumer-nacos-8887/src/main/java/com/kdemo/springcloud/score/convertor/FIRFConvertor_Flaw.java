package com.kdemo.springcloud.score.convertor;

import com.kdemo.springcloud.score.ScoreConvertor;

/**
 * First in rank first convertor
 * Simple version, this only workable for small number of external Score
 */
@Deprecated
public class FIRFConvertor_Flaw implements ScoreConvertor {

    private static final long BIG_NUM = Integer.MAX_VALUE * 10L;

    private static final int REMAIN_DIGIT = 11;


    @Override
    public Double convertToZSetScore(Long externalScore, Long timestamp) {

        // fix big num - current timestamp
        long curTimestampInSec = timestamp / 1000;
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
    public Long convertFromZSetScore(Double cacheScore) {
        // just cut the decimal part
        String[] split = cacheScore.toString().split("\\.");
        return Long.parseLong(split[0]);
    }
}
