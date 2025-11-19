package com.assignment4.test;

import com.assignment4.tasks.LamportTimestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class LamportTimestampTest {
    LamportTimestamp cl1 = new LamportTimestamp(0);
    int timestamp;
    @Test
    void tick() {
        cl1.tick();
        timestamp = cl1.getCurrentTimestamp();
        Assertions.assertEquals(1, timestamp);
    }

    @Test
    void updateClock() {
        cl1.updateClock(2);
        timestamp=cl1.getCurrentTimestamp();
        Assertions.assertEquals(2, timestamp);
    }
}
