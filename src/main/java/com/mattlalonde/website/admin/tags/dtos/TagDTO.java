package com.mattlalonde.website.admin.tags.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TagDTO {
    private UUID id;
    private String name;
    private String description;
}
