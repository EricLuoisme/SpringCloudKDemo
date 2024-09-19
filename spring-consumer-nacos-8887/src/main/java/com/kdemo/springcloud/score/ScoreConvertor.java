package com.kdemo.springcloud.score;

/**
 * Score convertor
 */
public interface ScoreConvertor {

    /**
     * Convert external_score to cached_score
     *
     * @param externalScore external score
     * @return cached_score
     */
    Double convertToZSetScore(Integer externalScore);

    /**
     * Convert cached_score back to external_score
     *
     * @param cacheScore cached score
     * @return external_score
     */
    Integer convertFromZSetScore(Double cacheScore);

}
