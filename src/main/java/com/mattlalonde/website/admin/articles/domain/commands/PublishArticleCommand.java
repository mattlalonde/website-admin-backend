package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class PublishArticleCommand {
    @NonNull private UUID id;
    @NonNull private LocalDateTime publishDate;
}
