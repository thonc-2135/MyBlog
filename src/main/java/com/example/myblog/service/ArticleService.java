package com.example.myblog.service;

import com.example.myblog.domain.article.ArticleDto;
import com.example.myblog.domain.model.ArticleQueryParam;
import com.example.myblog.domain.model.FeedParams;
import com.example.myblog.security.AuthUserDetails;

import java.util.List;

public interface ArticleService {
    ArticleDto createArticle(final ArticleDto article, final AuthUserDetails authUserDetails);

    ArticleDto getArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto updateArticle(final String slug, final ArticleDto.Update article, final AuthUserDetails authUserDetails);

    void deleteArticle(final String slug, final AuthUserDetails authUserDetails);

    List<ArticleDto> feedArticles(final AuthUserDetails authUserDetails, final FeedParams feedParams);

    ArticleDto favoriteArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto unfavoriteArticle(final String slug, final AuthUserDetails authUserDetails);

    List<ArticleDto> listArticle(final ArticleQueryParam articleQueryParam, final AuthUserDetails authUserDetails);
}
