package CodeMaker.togetherLion.domain.post.controller;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.service.PostService;
import CodeMaker.togetherLion.domain.post.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/posts")
public class SearchController {

    private final PostService postService;
    private final SearchService searchService;

    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> searchPost(@PathVariable String searchText) {
        List<PostRes> posts = searchService.searchPost(searchText);
        return ResponseEntity.ok(posts);
    }
}
