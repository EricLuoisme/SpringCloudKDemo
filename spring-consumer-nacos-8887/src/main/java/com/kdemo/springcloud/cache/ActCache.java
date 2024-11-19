package com.kdemo.springcloud.cache;

import com.kdemo.springcloud.dto.ActivityInfo;

/**
 * Interface for activity cache
 */
public interface ActCache {

    // current activity
    String CUR_ACT = "CUR_ACT";


    /**
     * Get Activity info (inner cache logic)
     *
     * @return act info
     */
    ActivityInfo getActivityInfo();

    /**
     * Clear all cache
     */
    void clearActivityCache(String actNo);

    /**
     * Clear local cache only
     */
    void clearActivityLocalCache(String actNo);

}
