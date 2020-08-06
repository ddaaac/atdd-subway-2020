package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;

@WebMvcTest(controllers = MapController.class)
public class PathDocumentation extends Documentation {
    @Autowired
    private MapController mapController;

    @MockBean
    private MapService mapService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @Test
    void findPath() {
        Long sourceId = 1L;
        Long targetId = 2L;
        String type = "DISTANCE";
        List<StationResponse> stations = Arrays.asList(
            new StationResponse(1L, "삼송역", LocalDateTime.now(), LocalDateTime.now()),
            new StationResponse(2L, "지축역", LocalDateTime.now(), LocalDateTime.now()));

        when(
            mapService.findPath(any(LoginMember.class), anyLong(), anyLong(), any())
        ).thenReturn(new PathResponse(stations, 10, 20, 100));

        given().log().all().
            header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={source}&target={target}&type={type}", sourceId, targetId, type).
            then().
            log().all().
            apply(document("maps/find-path",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                    parameterWithName("source").description("출발역 아이디"),
                    parameterWithName("target").description("도착역 아이디"),
                    parameterWithName("type").description("계산 기준 - DISTANCE OR DURATION")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer auth credentials").optional()
                ),
                responseFields(
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                    fieldWithPath("fare").type(JsonFieldType.NUMBER).description("운임 요금"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("경 노드 id"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("경로 노드 이름")
                ))).
            extract();
    }
}
