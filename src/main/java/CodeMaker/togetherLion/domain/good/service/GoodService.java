package CodeMaker.togetherLion.domain.good.service;

import CodeMaker.togetherLion.domain.good.dto.GoodReq;
import CodeMaker.togetherLion.domain.good.dto.GoodRes;
import CodeMaker.togetherLion.domain.good.entity.Good;
import CodeMaker.togetherLion.domain.good.repository.GoodRepository;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final PostRepository postRepository;

    public GoodRes createGood(GoodReq goodReq) {
        User user = userRepository.findById(goodReq.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음!"));

        Post post = postRepository.findByPostId(goodReq.postId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));

        // Good 객체 생성 시 likeCheck 값을 여기서 직접 true로 설정
        Good good = Good.builder()
                .user(user)
                .post(post)
                .likeCheck(true) // likeCheck 값을 true로 설정
                .build();

        good = goodRepository.save(good);

        return GoodRes.fromEntity(good);
    }


    public void updateLikeCheck(int userId, int postId) {
        Good good = goodRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좋아요를 찾을 수 없습니다."));

        good.update(false);
        goodRepository.save(good);
    }

    public void againLikeCheckToFalse(int userId, int postId) {
        Good good = goodRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좋아요를 찾을 수 없습니다."));

        good.update(true);
        goodRepository.save(good);
    }

    public List<Post> findLikedPostsByUserId(int userId) {
        return goodRepository.findLikedPostsByUserId(userId);
    }

}
