package com.example.myblog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleQueryParam extends FeedParams {
    private String tag;
    private String author;
    private String favorited;
}
