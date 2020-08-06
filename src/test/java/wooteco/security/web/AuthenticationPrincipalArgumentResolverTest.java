package wooteco.security.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.members.member.domain.LoginMember;

@Import(MapController.class)
class AuthenticationPrincipalArgumentResolverTest {
    private AuthenticationPrincipalArgumentResolver resolver;

    private ServletWebRequest servletWebRequest;

    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        resolver = new AuthenticationPrincipalArgumentResolver();
        servletWebRequest = new ServletWebRequest(new MockHttpServletRequest());
        methodParameter = new MethodParameter(MapController.class.getMethod("findPath", Long.class, Long.class, PathType.class, LoginMember.class), 3);
    }

    @DisplayName("MapController의 findPath 메서드의 LoginMember를 ArgumentResolver가 지원하는지 확인한다.")
    @Test
    void resolverSupportLoginMemberInFindPath() {
        boolean isSupportParameter = resolver.supportsParameter(methodParameter);
        assertThat(isSupportParameter).isTrue();
    }

    @DisplayName("비로그인 사용자일 경우 LoginMember 객체 대신 null이 반환되는지 확인한다.")
    @Test
    void nullReturnForNotLoginMember() {
        assertThat(resolver.resolveArgument(methodParameter, null, servletWebRequest, null))
            .isNull();
    }
}
