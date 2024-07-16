package com.rok.repository;

import com.rok.domain.Post;
import com.rok.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
