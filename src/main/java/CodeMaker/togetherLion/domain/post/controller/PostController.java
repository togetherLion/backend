package CodeMaker.togetherLion.domain.post.controller;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.service.PostService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final SessionUtil sessionUtil;

    @PostMapping("")
    public ResponseEntity<PostRes> createPost(@RequestBody Post post, HttpServletRequest request) {
        Post createdPost = postService.createPost(post, request);
        PostRes postRes = PostRes.fromEntity(createdPost);
        return ResponseEntity.ok(postRes);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        List<PostRes> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostRes> getPost(@PathVariable Integer postId) {
        try {
            Post post = postService.getPost(postId);
            PostRes postRes = PostRes.fromEntity(post);
            return ResponseEntity.ok(postRes);
        } catch (IllegalArgumentException e) {
            log.error("Error while fetching post: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PutMapping("/{postId}")
    public ResponseEntity<PostRes> updatePost(@PathVariable Integer postId, @RequestBody PostReq postReq) {
        Post post = postService.updatePost(postId,postReq);
        PostRes postRes = PostRes.fromEntity(post);
        return ResponseEntity.ok(postRes);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 사용자가 속한 지역 게시글 조회 (메인 화면)
//    @GetMapping("")
//    public ResponseEntity<?> getRegionPosts(HttpServletRequest request) {
//        int userId = sessionUtil.getUserIdFromSession(request);
//        List<PostRes> posts = postService.getRegionPosts(userId);
//        return ResponseEntity.ok(posts);
//    }
}
