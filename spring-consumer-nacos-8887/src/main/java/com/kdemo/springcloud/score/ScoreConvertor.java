package com.kdemo.springcloud.score;

/**
 * Score convertor
 */
public interface ScoreConvertor {

    /**
     * Convert external_score to cached_score
     *
     * @param externalScore external score
     * @param timestamp     in ms
     * @return cached_score
     */
    Double convertToZSetScore(Long externalScore, Long timestamp);

    /**
     * Convert cached_score back to external_score
     *
     * @param cacheScore cached score
     * @return external_score
     */
    Long convertFromZSetScore(Double cacheScore);

}
