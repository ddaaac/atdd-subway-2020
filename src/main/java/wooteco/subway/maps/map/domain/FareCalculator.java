package wooteco.subway.maps.map.domain;

import wooteco.subway.members.member.domain.MemberAgeType;

public interface FareCalculator {
    int calculate(int distance, int lineExtraFare, MemberAgeType memberAgeType);
}
