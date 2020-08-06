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

    public int calculateFare(Map<Long, Integer> extraFares, MemberAgeType memberType) {
        int total = calculateDistanceFare();
        total += calculateExtraFareByLine(extraFares);
        total = memberType.reviseFare(total);

        return total;
    }

    private int calculateExtraFareByLine(final Map<Long, Integer> extraFares) {
        return lineStationEdges.stream()
            .mapToInt(it -> extraFares.get(it.getLineId()))
            .max()
            .orElseThrow(() -> new RuntimeException("Line이 존재하지 않습니다."));
    }

    private int calculateDistanceFare() {
        int distance = calculateDistance();
        int totalFare = ZERO;

        if (distance > FIFTY) {
            totalFare += getExtraFare(distance - FIFTY, OVER_FIFTY_EXTRA_FARE_UNIT);
        }
        if (distance > TEN) {
            int extraDistance = getExtra10FareDistance(distance);
            totalFare += getExtraFare(extraDistance, OVER_TEN_EXTRA_FARE_UNIT);
        }
        totalFare += DEFAULT_FARE;
        return totalFare;
    }

    private int getExtraFare(int extraDistance, int unit) {
        return ((extraDistance - 1) / unit + 1) * EXTRA_FARE_MULTIPLE;
    }

    private int getExtra10FareDistance(int distance) {
        if (distance > FIFTY) {
            return FIFTY - TEN;
        }
        return distance - TEN;
    }
}
