package wooteco.subway.members.member.domain;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum MemberAgeType {
    ADULT(age -> age >= 19, fare -> fare),
    YOUTH(age -> age >= 13 && age < 19, fare -> fare - (int)((fare - 350) * 0.2)),
    CHILD(age -> age < 13, fare -> fare - (int)((fare - 350) * 0.5));

    private Predicate<Integer> agePolicy;
    private Function<Integer, Integer> discountPolicy;

    MemberAgeType(Predicate<Integer> agePolicy, Function<Integer, Integer> discountPolicy) {
        this.agePolicy = agePolicy;
        this.discountPolicy = discountPolicy;
    }

    public static MemberAgeType getLoginMemberAgeType(LoginMember loginMember) {
        if (Objects.isNull(loginMember)) {
            return ADULT;
        }
        return Stream.of(values())
            .filter(type -> type.agePolicy.test(loginMember.getAge()))
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

    public int calculateFare(int fare) {
        return discountPolicy.apply(fare);
    }
}
