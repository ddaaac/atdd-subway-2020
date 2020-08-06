package wooteco.subway.maps.map.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import wooteco.subway.members.member.domain.MemberAgeType;

public class SubwayPath {
    private static final int ZERO = 0;
    private static final int FIFTY = 50;
    private static final int TEN = 10;
    private static final int OVER_FIFTY_EXTRA_FARE_UNIT = 8;
    private static final int OVER_TEN_EXTRA_FARE_UNIT = 5;
    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_FARE_MULTIPLE = 100;

    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
            .map(it -> it.getLineStation().getStationId())
            .collect(Collectors.toList()));

        return stationIds;
    }

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public int getMaxExtraFare(final Map<Long, Integer> extraFares) {
        return lineStationEdges.stream()
            .mapToInt(it -> extraFares.get(it.getLineId()))
            .max()
            .orElseThrow(() -> new RuntimeException("Line이 존재하지 않습니다."));
    }
}
