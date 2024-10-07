package com.kdemo.springcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Complex game info dto simulation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoDto implements Serializable {

    private boolean notInSeason;

    private String gameId;

    private String gameName;

    private String gameLink;
}
