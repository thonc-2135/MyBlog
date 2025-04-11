package com.example.myblog.service;

import com.example.myblog.domain.comment.CommentDto;
import com.example.myblog.security.AuthUserDetails;

import java.util.List;

public interface CommentService {
    CommentDto addCommentsToAnArticle(final String slug, final CommentDto comment, final AuthUserDetails authUserDetails);

    void delete(final String slug, final Long commentId, final AuthUserDetails authUserDetails);

    List<CommentDto> getCommentsBySlug(final String slug, final AuthUserDetails authUserDetails);
}
