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

class SubwayPathTest {
    private List<Line> lines;
    private Map<Long, Integer> extraFares;
    private List<LineStation> lineStations;

    @BeforeEach
    void setUp() {
        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        LineStation lineStation1 = new LineStation(1L, null, 50, 0);
        line1.addLineStation(lineStation1);

        Line line4 = TestObjectUtils.createLine(4L, "4호선", "BLUE", 1000);
        LineStation lineStation8 = new LineStation(2L, null, 3, 0);
        line4.addLineStation(lineStation8);

        Line line5 = TestObjectUtils.createLine(5L, "5호선", "PURPLE", 2000);
        LineStation lineStation10 = new LineStation(3L, 4L, 3, 0);
        line5.addLineStation(lineStation10);

        lines = Arrays.asList(line1, line4, line5);
        lineStations = Arrays.asList(lineStation1, lineStation8, lineStation10);

        extraFares = lines.stream()
            .collect(Collectors.toMap(Line::getId, Line::getExtraFare));
    }

    @DisplayName("최대 extra fare 값")
    @Test
    void defaultFare() {
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            new LineStationEdge(lineStations.get(0), lines.get(0).getId()),
            new LineStationEdge(lineStations.get(1), lines.get(1).getId()),
            new LineStationEdge(lineStations.get(2), lines.get(2).getId())
        );
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);

        int extraFare = subwayPath.getMaxExtraFare(extraFares);

        assertThat(extraFare).isEqualTo(2000);
    }

}
