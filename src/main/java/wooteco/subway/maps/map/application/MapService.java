package wooteco.subway.maps.map.application;

import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.maps.line.application.LineService;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.line.dto.LineStationResponse;
import wooteco.subway.maps.map.domain.FareCalculator;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.dto.PathResponseAssembler;
import wooteco.subway.maps.station.application.StationService;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.MemberAgeType;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MapService {
    private LineService lineService;
    private StationService stationService;
    private PathService pathService;
    private FareCalculator fareCalculator;

    public MapService(LineService lineService, StationService stationService, PathService pathService,
        final FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
        this.fareCalculator = fareCalculator;
    }

    public MapResponse findMap() {
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = findStations(lines);

        List<LineResponse> lineResponses = lines.stream()
                .map(it -> LineResponse.of(it, extractLineStationResponses(it, stations)))
                .collect(Collectors.toList());

        return new MapResponse(lineResponses);
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        List<Line> lines = lineService.findLines();
        Map<Long, Integer> extraFares = lines.stream()
            .collect(Collectors.toMap(Line::getId, Line::getExtraFare));
        SubwayPath subwayPath = pathService.findPath(lines, source, target, type);
        Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        MemberAgeType memberType = MemberAgeType.getLoginMemberAgeType(loginMember);

        return new PathResponseAssembler(fareCalculator)
            .assemble(subwayPath, stations, extraFares, memberType);
    }

    private Map<Long, Station> findStations(List<Line> lines) {
        List<Long> stationIds = lines.stream()
                .flatMap(it -> it.getStationInOrder().stream())
                .map(it -> it.getStationId())
                .collect(Collectors.toList());

        return stationService.findStationsByIds(stationIds);
    }

    private List<LineStationResponse> extractLineStationResponses(Line line, Map<Long, Station> stations) {
        return line.getStationInOrder().stream()
                .map(it -> LineStationResponse.of(line.getId(), it, StationResponse.of(stations.get(it.getStationId()))))
                .collect(Collectors.toList());
    }
}
