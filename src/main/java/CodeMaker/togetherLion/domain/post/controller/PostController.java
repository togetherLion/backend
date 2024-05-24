package CodeMaker.togetherLion.domain.post.controller;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<PostRes> createPost(@PathVariable Integer userId, @RequestBody PostReq postReq) {
        Post post = postReq.toEntity();
        Post createdPost = postService.createPost(userId, post);
        PostRes postRes = PostRes.fromEntity(createdPost);
        return ResponseEntity.ok(postRes);
    }

}
