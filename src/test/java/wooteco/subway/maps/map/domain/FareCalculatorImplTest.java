package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.members.member.domain.MemberAgeType.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.members.member.domain.MemberAgeType;

class FareCalculatorImplTest {
    private FareCalculatorImpl fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculatorImpl();
    }

    @DisplayName("운임 계산")
    @ParameterizedTest
    @MethodSource("generateFareInput")
    void calculateFare(int distance, int lineExtraFare, MemberAgeType memberAgeType, int expectedFare) {
        int fare = fareCalculator.calculate(distance, lineExtraFare, memberAgeType);

        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> generateFareInput() {
        return Stream.of(
            Arguments.of(10, 0, ADULT, 1250),
            Arguments.of(11, 0, ADULT, 1350),
            Arguments.of(50, 0, ADULT, 2050),
            Arguments.of(58, 0, ADULT, 2150),
            Arguments.of(58, 100, ADULT, 2250),
            Arguments.of(58, 0, ADULT, 2150),
            Arguments.of(8, 2000, CHILD, 1800)
        );
    }
}
