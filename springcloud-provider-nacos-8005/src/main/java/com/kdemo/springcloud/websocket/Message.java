package com.kdemo.springcloud.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roylic
 * 2022/5/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String from;
    private String to;
}
