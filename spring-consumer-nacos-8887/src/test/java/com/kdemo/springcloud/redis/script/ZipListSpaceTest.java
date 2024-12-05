package com.kdemo.springcloud.redis.script;

import com.kdemo.springcloud.helper.compress.ZipListSpaceMaximiser;
import org.junit.Test;

public class ZipListSpaceTest {

    private static final String BUCKET_PREFIX = "pri";

    private static final int TRAIL_SHIFT = 9;


    @Test
    public void checkMaximiser() {
        String singleKey_1 = "asdfa2134asdofha132412341";
        ZipListSpaceMaximiser.PartitionBundle partitionBundle_1 =
                ZipListSpaceMaximiser.partitionKeyIntoBuckets(BUCKET_PREFIX, singleKey_1, TRAIL_SHIFT);
        System.out.println("Key1 bucketKey: " + partitionBundle_1.bucketKey());
        System.out.println("Key1 memberKey: " + partitionBundle_1.memberKey());

        String singleKey_2 = "asdfa2134asdofha132412342";
        ZipListSpaceMaximiser.PartitionBundle partitionBundle_2 =
                ZipListSpaceMaximiser.partitionKeyIntoBuckets(BUCKET_PREFIX, singleKey_2, TRAIL_SHIFT);
        System.out.println("Key2 bucketKey: " + partitionBundle_2.bucketKey());
        System.out.println("Key2 memberKey: " + partitionBundle_2.memberKey());
    }
}
