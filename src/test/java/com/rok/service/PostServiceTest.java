package com.rok.service;

import com.rok.domain.Post;
import com.rok.exception.PostNotFound;
import com.rok.repository.PostRepository;
import com.rok.request.PostCreate;
import com.rok.request.PostEdit;
import com.rok.request.PostSearch;
import com.rok.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(postCreate);


        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("1234567890123456")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //when
        PostResponse postResponse = postService.get(requestPost.getId());


        //then
        assertNotNull(postResponse);
        assertEquals(1L, postRepository.count());
        assertEquals("1234567890", postResponse.getTitle());
        assertEquals("bar", postResponse.getContent());

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        //given

        List<Post> requestPosts = IntStream.range(0, 20) //stream관련 api
                .mapToObj(i->{
                    return Post.builder()
                            .title("foo " + i)
                            .content("bar " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        PostSearch postSearch = PostSearch.builder().build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("foo 19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();


        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .title("수정 foo")
                .content("bar")
                .build();

        //when
        postService.edit(requestPost.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(requestPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + requestPost.getId()));
        assertEquals("수정 foo", changePost.getTitle());
        assertEquals("bar", changePost.getContent());


    }
    @Test
    @DisplayName("글 삭제")
    void test5() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //when
        postService.delete(requestPost.getId());

        //then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test6() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(requestPost.getId() + 1L);
        });
    }

    @Test
    @DisplayName("존재하지 않는 글 삭제")
    void test7() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(requestPost.getId() + 1L);
        });


    }

}