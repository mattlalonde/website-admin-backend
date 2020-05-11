package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class UnpublishArticleCommand {
    @NonNull private UUID id;
}
