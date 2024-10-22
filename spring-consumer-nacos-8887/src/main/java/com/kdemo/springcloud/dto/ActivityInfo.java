package com.kdemo.springcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Complex activity info dto simulation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInfo implements Serializable {

    private boolean notInSeason;

    private String actId;

    private String actName;

    private String actLink;
}
