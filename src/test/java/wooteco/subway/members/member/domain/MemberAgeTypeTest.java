package wooteco.subway.members.member.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.subway.maps.map.acceptance.PathAcceptanceTest.*;
import static wooteco.subway.members.member.domain.MemberAgeType.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberAgeTypeTest {

    @DisplayName("Age에 맞는 멤버 타입을 리턴하고 올바르게 운임을 계산하는지 테스트")
    @ParameterizedTest
    @MethodSource("ageInput")
    void getMemberAndCalculate(int age, MemberAgeType expectedType, int fare, int expectedFare) {
        MemberAgeType loginMemberAgeType = getLoginMemberAgeType(new LoginMember(1L, EMAIL, PASSWORD, age));

        assertAll(
            () -> assertThat(loginMemberAgeType).isEqualTo(expectedType),
            () -> assertThat(loginMemberAgeType.calculateFare(fare)).isEqualTo(expectedFare)
        );
    }

    private static Stream<Arguments> ageInput() {
        return Stream.of(
            Arguments.of(12, CHILD, 1000, 675),
            Arguments.of(13, YOUTH, 1000, 870),
            Arguments.of(18, YOUTH, 2000, 1670),
            Arguments.of(19, ADULT, 2000, 2000)
        );
    }
}
