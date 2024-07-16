package com.rok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rok.domain.Post;
import com.rok.repository.PostRepository;
import com.rok.request.PostCreate;
import com.rok.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest // ?? mock을 사용가능하게 해줌
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void test() throws Exception {

/*
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
*/

/*
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다")
                        .param("content", "글 내용입니다 하하")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
*/

        PostCreate request = new PostCreate("제목입니다", "내용입니다");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title값을 필수이다")
    void test2() throws Exception {

        /*mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" : \"\", \"content\" : \"내용입니다\"}")// 값이 들어가는 방식알아보기
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());*/

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\" : \"\", \"content\" : \"내용입니다\"}")// 값이 들어가는 방식알아보기
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400")) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다")) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요.")) //오브젝트나 배열 검증해보기
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);// json형태로 가공

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)// 값이 들어가는 방식알아보기
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3")) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$.title").value("foo")) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$.content").value("bar")) //오브젝트나 배열 검증해보기

                .andDo(print());

    }

    /*@Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        Post post1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .build());

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2))) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[0].id").value(post1.getId())) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[0].title").value(post1.getTitle())) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[0].content").value(post1.getContent())) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[1].id").value(post2.getId())) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[1].title").value(post2.getTitle())) //오브젝트나 배열 검증해보기
                .andExpect(jsonPath("$[1].content").value(post2.getContent())) //오브젝트나 배열 검증해보기

                .andDo(print());

    }*/

    @Test
    @DisplayName("글 여러개 조회")
    void test6() throws Exception {
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

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo 19"))
                .andExpect(jsonPath("$[0].content").value("bar 19"))
                .andDo(print());

    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test7() throws Exception {
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

        // expected
        mockMvc.perform(get("/posts?page=0&size=5")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("foo 19"))
                .andExpect(jsonPath("$[0].content").value("bar 19"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 수정")
    void test8() throws Exception {
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


        // expected
        mockMvc.perform(patch("/posts/{postId}", requestPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        // expected
        mockMvc.perform(delete("/posts/{postId}", requestPost.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test10() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("foo")
                .content("bar")
                .build();

        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성지 제목에 '바보'는 포함될수 없다")
    void test11() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);// json형태로 가공

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)// 값이 들어가는 방식알아보기
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }



}

//SPRING RestDocs
// - 운영코드에 -> 영향
// - 코드 수정 -> 문서를 수정 x -> 코드(기능) <-> 문서
// - Test 케이스 실행 -> 문서를 생성해준다
