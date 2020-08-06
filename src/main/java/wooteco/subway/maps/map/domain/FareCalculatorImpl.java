package wooteco.subway.maps.map.domain;

import org.springframework.stereotype.Component;

import wooteco.subway.members.member.domain.MemberAgeType;

@Component
public class FareCalculatorImpl implements FareCalculator {
    private static final int ZERO = 0;
    private static final int FIFTY = 50;
    private static final int TEN = 10;
    private static final int OVER_FIFTY_EXTRA_FARE_UNIT = 8;
    private static final int OVER_TEN_EXTRA_FARE_UNIT = 5;
    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_FARE_MULTIPLE = 100;

    @Override
    public int calculate(int distance, int lineExtraFare, MemberAgeType memberAgeType) {
        int totalFare = ZERO;

        if (distance > FIFTY) {
            totalFare += getExtraFare(distance - FIFTY, OVER_FIFTY_EXTRA_FARE_UNIT);
        }
        if (distance > TEN) {
            int extraDistance = getExtra10FareDistance(distance);
            totalFare += getExtraFare(extraDistance, OVER_TEN_EXTRA_FARE_UNIT);
        }
        totalFare += DEFAULT_FARE;
        totalFare += lineExtraFare;
        return memberAgeType.reviseFare(totalFare);
    }

    private int getExtraFare(int extraDistance, int unit) {
        return ((extraDistance - 1) / unit + 1) * EXTRA_FARE_MULTIPLE;
    }

    private int getExtra10FareDistance(int distance) {
        if (distance > FIFTY) {
            return FIFTY - TEN;
        }
        return distance - TEN;
    }
}
