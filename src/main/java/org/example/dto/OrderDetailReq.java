package org.example.dto;

import java.util.List;
import java.util.UUID;

public record OrderDetailReq(List<UUID> books) {

}
