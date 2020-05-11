package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class UpdateArticleCommand {

    @NonNull private UUID id;
    @NonNull private String title;
    private String precis;
    private String body;
}
