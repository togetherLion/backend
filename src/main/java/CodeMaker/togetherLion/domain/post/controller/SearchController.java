package CodeMaker.togetherLion.domain.post.controller;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.service.PostService;
import CodeMaker.togetherLion.domain.post.service.SearchService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/posts")
public class SearchController {

    private final PostService postService;
    private final SearchService searchService;
    private final SessionUtil sessionUtil;

    // 게시글 검색 -> 사용자가 속한 지역 게시글 검색으로 변경
    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> searchPost(@PathVariable String searchText, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<PostRes> posts = searchService.searchPost(searchText, userId);
        return ResponseEntity.ok(posts);
    }

    // 최근 검색 기록
    @PostMapping("/recentSearch")
    public ResponseEntity<List<String>> recentSearch(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<String> searchTexts = searchService.recentSearch(userId);
        return ResponseEntity.ok(searchTexts);
    }

    // 인기 검색어
    @PostMapping("/bestSearch")
    public ResponseEntity<List<String>> bestSearch() {
        List<String> bestSearchs = searchService.bestSearch();
        return ResponseEntity.ok(bestSearchs);
    }

    // 조회 옵션 - 저가순
    @GetMapping("/search/low/{searchText}")
    public ResponseEntity<?> searchPostLowCost(@PathVariable String searchText, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<PostRes> posts = searchService.searchPostLowCost(searchText, userId);
        return ResponseEntity.ok(posts);
    }

    // 조회 옵션 - 고가순
    @GetMapping("/search/high/{searchText}")
    public ResponseEntity<?> searchPostHighCost(@PathVariable String searchText, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<PostRes> posts = searchService.searchPostHighCost(searchText, userId);
        return ResponseEntity.ok(posts);
    }
}
