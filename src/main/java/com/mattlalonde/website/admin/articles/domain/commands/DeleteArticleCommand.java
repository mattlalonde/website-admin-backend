package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class DeleteArticleCommand {
    @NonNull private UUID id;
}
