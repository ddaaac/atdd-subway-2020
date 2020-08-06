package wooteco.subway.maps.line.dto;

import wooteco.subway.maps.line.domain.Line;

import java.time.LocalTime;

public class LineRequest {
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer intervalTime;
    private Integer extraFee;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final LocalTime startTime, final LocalTime endTime,
        final Integer intervalTime,
        final Integer extraFee) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.extraFee = extraFee;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public Integer getExtraFee() {
        return extraFee;
    }

    public Line toLine() {
        return new Line(name, color, startTime, endTime, intervalTime, extraFee);
    }
}
