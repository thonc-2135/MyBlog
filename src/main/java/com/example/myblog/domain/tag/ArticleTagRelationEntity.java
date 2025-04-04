package com.example.myblog.domain.tag;

import com.example.myblog.domain.BaseEntity;
import com.example.myblog.domain.article.ArticleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class ArticleTagRelationEntity extends BaseEntity {
    @Column(name = "tag", nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article", nullable = false)
    private ArticleEntity article;
}
