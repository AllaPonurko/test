package org.example.dto;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OrderReq(@NotNull String userId,@NotNull OrderDetailReq orderDetailReq) {

}
