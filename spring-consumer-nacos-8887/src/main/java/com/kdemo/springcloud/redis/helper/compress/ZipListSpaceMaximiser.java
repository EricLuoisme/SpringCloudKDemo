package com.kdemo.springcloud.redis.helper.compress;

import org.springframework.util.StringUtils;

/**
 * Convert key into ZipList maximum size -> for space-saving
 */
public class ZipListSpaceMaximiser {

    /**
     * Partition one single key into different buckets
     *
     * @param bucketPrefix bucket prefix (should remain shorter)
     * @param singleKey    single long key
     * @param trailShift   trail shift, 9 is current best practice (< 512, 9 digits)
     * @return partition bundle
     */
    public static PartitionBundle partitionKeyIntoBuckets(String bucketPrefix, String singleKey, int trailShift) {

        if (!StringUtils.hasLength(singleKey)) {
            throw new IllegalArgumentException("singleKey should not be null");
        }

        // use hash code to 'fix' the length of key
        int hashVal = Math.abs(singleKey.hashCode());

        // how many digits should be remained as member key inside a bucket
        int trailMask = (1 << trailShift) - 1;

        // bucketKey is prefix +
        String bucketKey = bucketPrefix + (hashVal >> trailShift);
        String memberKey = String.valueOf(hashVal & trailMask);

        // hard coding exception
        if (bucketKey.length() > 64) {
            throw new IllegalStateException("Bucket key exceeds zipList limit: " + bucketKey);
        }

        return new PartitionBundle(bucketKey, memberKey);
    }


    /**
     * Partition bundle
     *
     * @param bucketKey bucket key
     * @param memberKey member key inside the bucket
     */
    public record PartitionBundle(String bucketKey, String memberKey) {
    }

}
