package com.example.growingshop.domain.auth.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Priority {
    private static final long  HIGHEST_PRIORITY = -1;

    @Column(nullable = false)
    private Long value;

    @Column
    private Long adjustment;

    public Priority(Long  value, Long adjustment) {
        this.value = value < HIGHEST_PRIORITY ? HIGHEST_PRIORITY : value;
        this.adjustment = adjustment;
    }

    public Priority(Long value) {
        this(value, null);
    }

    public boolean highestPriority(Priority target) {
        return getPriorityValue() < target.getPriorityValue();
    }

    private long getPriorityValue() {
        return value + getAdjustment();
    }

    private long getAdjustment() {
        if (adjustment == null) {
            return 0;
        }

        return this.adjustment;
    }
}
