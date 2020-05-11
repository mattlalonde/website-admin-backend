package com.mattlalonde.website.admin.articles.domain.commands;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class CreateArticleCommand {

    @NonNull  private UUID id;
    @NonNull  private String title;
    @NonNull  private LocalDateTime timeStamp;

}
