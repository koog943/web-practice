package com.rok.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rok.domain.Post;
import com.rok.domain.QPost;
import com.rok.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.rok.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffSet())
                .orderBy(post.id.desc())
                .fetch();
    }


}
