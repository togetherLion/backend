package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.user.service.UserInfoService;
import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;
import CodeMaker.togetherLion.domain.usersearch.repository.UserSearchRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class SearchService {

    private final PostRepository postRepository;
    private final UserSearchRepository userSearchRepository;
    private final UserRepository userRepository;

    // 게시글 조회
    public List<PostRes> searchPost(String searchText, int userId) {
        List<Post> posts = postRepository.searchBySearchText(searchText);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 userId입니다."));

        if(userSearchRepository.existsBySearchTextAndUser(searchText, user)) { // 중복되는 searchText 있으면 시간만 update
            UserSearch userSearch = userSearchRepository.findBySearchTextAndUser(searchText, user)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 검색 기록입니다."));

            userSearch.setSearchDate(LocalDateTime.now());
            userSearchRepository.save(userSearch);
        }
        else{ // 아예 처음 검색
            UserSearch userSearch2 = UserSearch.builder()
                    .user(user)
                    .searchDate(LocalDateTime.now())
                    .searchText(searchText)
                    .build();

            userSearchRepository.save(userSearch2);
        }

        return posts.stream()
                .map(PostRes::fromEntity)
                .collect(Collectors.toList());
    }
}
