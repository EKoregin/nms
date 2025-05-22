package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class PortCreateEditDto {

    @NotBlank
    String name;

    @NotNull
    Integer speed;

    @NotNull
    PortDuplex duplex;

    @NotNull
    MediaType mediaType;

    @NotNull
    PortType type;

    Long deviceId;

    Long modelId;

    Long linkId;
}
