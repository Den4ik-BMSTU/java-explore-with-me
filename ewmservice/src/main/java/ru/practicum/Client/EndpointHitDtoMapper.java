package ru.practicum.Client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointHitDtoMapper {

    public static EndpointHitDto toEndpointHitDto(String ip, String uri) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setIp(ip);
        endpointHitDto.setApp("ewm-main-service");
        endpointHitDto.setUri(uri);
        endpointHitDto.setTimestamp(localDateTimeToString(LocalDateTime.now()));
        return endpointHitDto;
    }

    public static String localDateTimeToString(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localDateTime = timestamp.format(formatter);
        return localDateTime;
    }

}