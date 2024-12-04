package com.kdemo.springcloud.helper.score.convertor;

import com.kdemo.springcloud.helper.score.ScoreConvertor;

/**
 * The workable version with First in Rank first implementation
 * p.s. Double's significand precision is 52 bit
 */
public class FIRFConvertor implements ScoreConvertor {

    /* deviation of the delta-timestamp input -> saving the bit for score usage */
    private static final int DEVIATION = 1000;

    private static final int TOTAL_BIT = 52;

    private static final int SCORE_BIT = 24;

    private static final int TIMESTAMP_BIT = TOTAL_BIT - SCORE_BIT;

    private static final long MAX_SCORE = (1L << SCORE_BIT) - 1;

    private static final long MAX_TIMESTAMP = (1L << TIMESTAMP_BIT) - 1;


    /**
     * Split 52 bits into two part
     * 1) high 20 bits -> for the score
     * 2) low 32 bits -> for a delta-timestamp = newTimestamp - baseTimestamp
     *
     * @param externalScore external score
     * @param timestamp     delta timestamp
     * @return combined score follow first in rank first logic
     */
    @Override
    public Double convertToZSetScore(Long externalScore, Long timestamp) {

        if (externalScore < 0 || externalScore > MAX_SCORE) {
            throw new IllegalArgumentException("score overflow");
        }

        long useTimestamp = timestamp / DEVIATION;
        if (useTimestamp < 0 || useTimestamp > MAX_TIMESTAMP) {
            throw new IllegalArgumentException("timestamp overflow");
        }

        // move score to higher & fill lower with timestamp
        long combinedScore = (externalScore << TIMESTAMP_BIT) | useTimestamp;
        return (double) combinedScore;
    }

    @Override
    public Long convertFromZSetScore(Double cacheScore) {
        // move back to lower bits
        return cacheScore.longValue() >> TIMESTAMP_BIT;
    }
}
