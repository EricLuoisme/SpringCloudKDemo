package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoHashTest {

    /**
     * 地理位置数据
     */
    @Test
    public void geoHashTest() {

        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        // create geo-key
        RGeo<String> geo = redisson.getGeo("myGeo");

        // two ways to create new locs
        GeoEntry entry = new GeoEntry(13.361389, 38.115556, "Palermo");
        geo.add(entry);
        geo.add(15.087269, 37.502669, "Catania");

        // get distance between 2 locs
        Double dist = geo.dist("Palermo", "Catania", GeoUnit.METERS);

        Map<String, GeoPosition> pos = geo.pos("Palermo", "Catania");

        // specified a circle for getting all inside locs
        List<String> cities = geo.radius(15, 37, 200, GeoUnit.KILOMETERS);

        // specified a loc find all surrounding city
        List<String> allNearCities = geo.radius("Palermo", 10, GeoUnit.KILOMETERS);

        Map<String, Double> citiesWithDistance = geo.radiusWithDistance(15, 37, 200, GeoUnit.KILOMETERS);
        Map<String, Double> allNearCitiesDistance = geo.radiusWithDistance("Palermo", 10, GeoUnit.KILOMETERS);

        Map<String, GeoPosition> citiesWithPosition = geo.radiusWithPosition(15, 37, 200, GeoUnit.KILOMETERS);
        Map<String, GeoPosition> allNearCitiesPosition = geo.radiusWithPosition("Palermo", 10, GeoUnit.KILOMETERS);

        redisson.shutdown();
    }
}
