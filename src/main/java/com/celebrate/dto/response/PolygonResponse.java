package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolygonResponse {
    List<List<List<Double>>> coordinates;
}
