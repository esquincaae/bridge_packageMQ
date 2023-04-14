package com.example.bridge.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class UpdateEventRequest implements Serializable {



    private String type;
    private String trackingId;

}
