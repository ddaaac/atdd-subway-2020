package wooteco.subway.maps.map.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.subway.maps.map.domain.FareCalculator;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.MemberAgeType;

public class PathResponseAssembler {
    private FareCalculator fareCalculator;

    public PathResponseAssembler(final FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations,
        Map<Long, Integer> extraFares, MemberAgeType memberType) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
            .map(it -> StationResponse.of(stations.get(it)))
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int lineExtraFare = subwayPath.getMaxExtraFare(extraFares);
        int fare = fareCalculator.calculate(distance, lineExtraFare, memberType);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }
}
