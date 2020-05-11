package com.mattlalonde.website.admin.tags.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class UpdateTagCommand {
    @NonNull private UUID id;
    @NonNull private String name;
    private String description;
}
