package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class AddArticleTagCommand {
    @NonNull private UUID articleId;
    @NonNull private UUID tagId;
}
