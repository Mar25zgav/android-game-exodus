package com.example.exodus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInRanges {

    private final List<Float> range = new ArrayList<>();

    RandomInRanges(float min, float max) {
        this.addRange(min, max);
    }

    final void addRange(float min, float max) {
        for (float i = min; i <= max; i++) {
            this.range.add(i);
        }
    }

    float getRandom() {
        return this.range.get(new Random().nextInt(this.range.size()));
    }

}
