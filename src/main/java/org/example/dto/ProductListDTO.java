package org.example.dto;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ProductListDTO(@NotNull UUID idProduct, @NotNull long quantity) {
}
