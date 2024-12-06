package com.kdemo.springcloud.redis.script;

import com.kdemo.springcloud.redis.helper.FIFORankingHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FIFORankingHelperTest {

    private static final String HASH_KEY = "scorePool";

    private static final String BOARD_KEY = "leaderboard";

    private static final int LEADERBOARD_SIZE = 299;

    private static final long BASE_TIMESTAMP = Instant.now().toEpochMilli();


    @Autowired
    private FIFORankingHelper rankingHelper;


    @Test
    public void checkUpdateRankingWholeLogic() {
        FIFORankingHelper.ScoreUploadResult uploadResult = rankingHelper.uploadScoreForRanking(
                BOARD_KEY, LEADERBOARD_SIZE, HASH_KEY, BASE_TIMESTAMP, 300, "user_12341234123", 10085L);
        System.out.println(uploadResult.leaderboardResult());
        System.out.println(uploadResult.scorePoolResult());
    }


}
