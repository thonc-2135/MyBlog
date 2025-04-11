package com.example.myblog.service;

import com.example.myblog.domain.BaseEntity;
import com.example.myblog.domain.article.ArticleDto;
import com.example.myblog.domain.article.ArticleEntity;
import com.example.myblog.domain.article.ArticleRepository;
import com.example.myblog.domain.article.FavoriteEntity;
import com.example.myblog.domain.model.ArticleQueryParam;
import com.example.myblog.domain.model.FeedParams;
import com.example.myblog.domain.profile.FollowEntity;
import com.example.myblog.domain.profile.FollowRepository;
import com.example.myblog.domain.profile.ProfileDto;
import com.example.myblog.domain.tag.ArticleTagRelationEntity;
import com.example.myblog.domain.user.UserEntity;
import com.example.myblog.exception.AppException;
import com.example.myblog.exception.Error;
import com.example.myblog.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;

    private final ProfileService profileService;

    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto article, AuthUserDetails authUserDetails) {
        String slug = String.join("-", article.getTitle().split(" "));
        UserEntity author = UserEntity.builder()
                .id(authUserDetails.getId())
                .build();

        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(slug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();
        List<ArticleTagRelationEntity> tagList = new ArrayList<>();
        for (String tag : article.getTagList()) {
            tagList.add(ArticleTagRelationEntity.builder().article(articleEntity).tag(tag).build());
        }
        articleEntity.setTagList(tagList);

        articleEntity = articleRepository.save(articleEntity);
        return convertEntityToDto(articleEntity, false, 0L, authUserDetails);
    }

    @Override
    public ArticleDto getArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        List<FavoriteEntity> favorites = found.getFavoriteList();
        Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
        int favoriteCount = favorites.size();
        return convertEntityToDto(found, favorited, (long) favoriteCount, authUserDetails);
    }

    private ArticleDto convertEntityToDto(ArticleEntity entity, Boolean favorited, Long favoritesCount, AuthUserDetails authUserDetails) {
        ProfileDto author = profileService.getProfileByUserId(entity.getAuthor().getId(), authUserDetails);
        return ArticleDto.builder()
                .slug(entity.getSlug())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .body(entity.getBody())
                .author(author)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .favorited(favorited)
                .favoritesCount(favoritesCount)
                .tagList(entity.getTagList().stream().map(ArticleTagRelationEntity::getTag).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public ArticleDto updateArticle(String slug, ArticleDto.Update article, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        if (article.getTitle() != null) {
            String newSlug = String.join("-", article.getTitle().split(" "));
            found.setTitle(article.getTitle());
            found.setSlug(newSlug);
        }

        if (article.getDescription() != null) {
            found.setDescription(article.getDescription());
        }

        if (article.getBody() != null) {
            found.setBody(article.getBody());
        }

        articleRepository.save(found);

        return getArticle(slug, authUserDetails);
    }

    @Transactional
    @Override
    public void deleteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        articleRepository.delete(found);
    }

    @Override
    public List<ArticleDto> feedArticles(AuthUserDetails authUserDetails, FeedParams feedParams) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(authUserDetails.getId()).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());
        return articleRepository.findByAuthorIdInOrderByCreatedAtDesc(feedAuthorIds, PageRequest.of(feedParams.getOffset(), feedParams.getLimit())).stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
            int favoriteCount = favorites.size();
            return convertEntityToDto(entity, favorited, (long) favoriteCount, authUserDetails);
        }).collect(Collectors.toList());
    }

    @Override
    public ArticleDto favoriteArticle(String slug, AuthUserDetails authUserDetails) {
        return null;
    }

    @Override
    public ArticleDto unfavoriteArticle(String slug, AuthUserDetails authUserDetails) {
        return null;
    }


    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> listArticle(ArticleQueryParam articleQueryParam, AuthUserDetails authUserDetails) {
        Pageable pageable = null;
        if (articleQueryParam.getOffset() != null) {
            pageable = PageRequest.of(articleQueryParam.getOffset(), articleQueryParam.getLimit());
        }

        List<ArticleEntity> articleEntities;
        if (articleQueryParam.getTag() != null) {
            articleEntities = articleRepository.findByTag(articleQueryParam.getTag(), pageable);
        } else if (articleQueryParam.getAuthor() != null) {
            articleEntities = articleRepository.findByAuthorName(articleQueryParam.getAuthor(), pageable);
        } else if (articleQueryParam.getFavorited() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(articleQueryParam.getFavorited(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return convertToArticleList(articleEntities, authUserDetails);
    }

    private List<ArticleDto> convertToArticleList(List<ArticleEntity> articleEntities, AuthUserDetails authUserDetails) {
        return articleEntities.stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
            int favoriteCount = favorites.size();
            return convertEntityToDto(entity, favorited, (long) favoriteCount, authUserDetails);
        }).collect(Collectors.toList());
    }
}
