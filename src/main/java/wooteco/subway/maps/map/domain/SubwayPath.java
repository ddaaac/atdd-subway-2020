package wooteco.subway.maps.map.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class SubwayPath {
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

    public int calculateFare(Map<Long, Integer> extraFares) {
        int total = calculateDistanceFare();
        total += calculateExtraFare(extraFares);

        return total;
    }

    private int calculateExtraFare(final Map<Long, Integer> extraFares) {
        return lineStationEdges.stream()
            .mapToInt(it -> extraFares.get(it.getLineId()))
            .max()
            .orElseThrow(() -> new RuntimeException("Line이 존재하지 않습니다."));
    }

    private int calculateDistanceFare() {
        int distance = calculateDistance();
        int total = 0;

        if (distance > 50) {
            total += ((distance - 50) / 8 + 1) * 100;
        }
        if (distance > 10) {
            int extraDistance = distance > 50 ? 40 : (distance - 10);
            total += (extraDistance / 5 + 1) * 100;
        }
        total += 1250;
        return total;
    }
}
