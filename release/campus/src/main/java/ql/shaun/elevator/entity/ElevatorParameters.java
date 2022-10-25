package ql.shaun.elevator.entity;

import lombok.Data;

@Data
public class ElevatorParameters {
    private String ip;
    private int port;
    private String snNum;
    private String password;

}
