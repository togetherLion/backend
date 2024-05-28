package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class SearchService {

    private final PostRepository postRepository;

    // 게시글 조회
    public List<PostRes> searchPost(String searchText) {
        List<Post> posts = postRepository.searchBySearchText(searchText);
        return posts.stream()
                .map(PostRes::fromEntity)
                .collect(Collectors.toList());
    }
}
