package org.example.dto;

import org.jetbrains.annotations.NotNull;

public record EmailReq(@NotNull String sendTo, String theme,String body) {
}
