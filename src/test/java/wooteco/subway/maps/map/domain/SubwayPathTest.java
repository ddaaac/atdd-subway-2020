package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import wooteco.subway.common.TestObjectUtils;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.line.domain.LineStation;
import wooteco.subway.members.member.domain.MemberAgeType;

class SubwayPathTest {
    private List<LineStation> lineStations;
    private List<Line> lines;
    private Map<Long, Integer> extraFares;

    @BeforeEach
    void setUp() {
        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        LineStation lineStation1 = new LineStation(1L, null, 50, 0);
        line1.addLineStation(lineStation1);
        LineStation lineStation2 = new LineStation(2L, 1L, 8, 2);
        line1.addLineStation(lineStation2);

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0);
        LineStation lineStation3 = new LineStation(2L, null, 10, 0);
        line2.addLineStation(lineStation3);
        LineStation lineStation4 = new LineStation(3L, 2L, 4, 1);
        line2.addLineStation(lineStation4);

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        LineStation lineStation5 = new LineStation(1L, null, 8, 0);
        line3.addLineStation(lineStation5);
        LineStation lineStation6 = new LineStation(4L, 1L, 2, 2);
        line3.addLineStation(lineStation6);
        LineStation lineStation7 = new LineStation(3L, 4L, 1, 2);
        line3.addLineStation(lineStation7);

        Line line4 = TestObjectUtils.createLine(4L, "4호선", "BLUE", 1000);
        LineStation lineStation8 = new LineStation(2L, null, 3, 0);
        line4.addLineStation(lineStation8);
        LineStation lineStation9 = new LineStation(4L, 1L, 2, 2);
        line4.addLineStation(lineStation9);

        Line line5 = TestObjectUtils.createLine(5L, "5호선", "PURPLE", 2000);
        LineStation lineStation10 = new LineStation(3L, 4L, 3, 0);
        line5.addLineStation(lineStation10);

        lines = Arrays.asList(line1, line2, line3, line4, line5);
        lineStations = Arrays.asList(lineStation1, lineStation2, lineStation3, lineStation4, lineStation5,
            lineStation6, lineStation7, lineStation8, lineStation9, lineStation10);

        extraFares = lines.stream()
            .collect(Collectors.toMap(Line::getId, Line::getExtraFare));
    }

    @DisplayName("기본 운임")
    @Test
    void defaultFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(5), lines.get(2).getId()),
            new LineStationEdge(lineStations.get(6), lines.get(2).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int fare = subwayPath.calculateFare(extraFares, MemberAgeType.ADULT);

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10~50 추가 운임")
    @Test
    void secondFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(2), lines.get(1).getId()),
            new LineStationEdge(lineStations.get(3), lines.get(1).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int fare = subwayPath.calculateFare(extraFares, MemberAgeType.ADULT);

        assertThat(fare).isEqualTo(1350);
    }

    @DisplayName("50~ 추가 운임")
    @Test
    void thireFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(0), lines.get(0).getId()),
            new LineStationEdge(lineStations.get(1), lines.get(0).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int fare = subwayPath.calculateFare(extraFares, MemberAgeType.ADULT);

        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("노선 추가 운임")
    @Test
    void lineExtraFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(7), lines.get(3).getId()),
            new LineStationEdge(lineStations.get(8), lines.get(3).getId()),
            new LineStationEdge(lineStations.get(9), lines.get(4).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int fare = subwayPath.calculateFare(extraFares, MemberAgeType.ADULT);

        assertThat(fare).isEqualTo(3250);
    }

    @DisplayName("나이에 따라 다른 요금")
    @Test
    void ageFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(7), lines.get(3).getId()),
            new LineStationEdge(lineStations.get(8), lines.get(3).getId()),
            new LineStationEdge(lineStations.get(9), lines.get(4).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int fare = subwayPath.calculateFare(extraFares, MemberAgeType.CHILD);

        assertThat(fare).isEqualTo(1800);
    }
}
