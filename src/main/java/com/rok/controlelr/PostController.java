package com.rok.controlelr;

import com.rok.domain.Post;
import com.rok.exception.InvalidRequest;
import com.rok.request.PostCreate;
import com.rok.request.PostEdit;
import com.rok.request.PostSearch;
import com.rok.response.PostResponse;
import com.rok.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


/*
    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
*/

/*
    @PostMapping("/posts")
    public String post(@RequestParam String title,
                       @RequestParam String content) {
        log.info("title={}, content={}", title, content);
        return "Hello World";
    }
*/

/*
    @PostMapping("/posts")
    public String post(@RequestParam Map<String, String> params) {
        log.info("params={}", params);
        return "Hello World";
    }
*/
/*

    @PostMapping("/posts")
    public String post(@ModelAttribute PostCreate params) {
        log.info("params={}", params.toString());
        return "Hello World";
    }
*/

/*
    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) {

        log.info("params={}", params.toString());
        return "Hello World";
    }
*/

    /*@PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params,
                       BindingResult result) throws Exception {
        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String filedName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();


            Map<String, String> error = new HashMap<>();
            error.put(filedName, errorMessage);
            return error;
        }

        return Map.of();  //?
    }*/

    /*@PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate request) throws Exception {
        postService.write(request);
        return Map.of();  //?
    }*/

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        request.isValid();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

/*
    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable) {
        return postService.getList(pageable);
    }
*/
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }




}
