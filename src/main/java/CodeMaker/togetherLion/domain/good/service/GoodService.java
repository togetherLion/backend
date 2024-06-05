package CodeMaker.togetherLion.domain.good.service;

import CodeMaker.togetherLion.domain.good.dto.GoodReq;
import CodeMaker.togetherLion.domain.good.dto.GoodRes;
import CodeMaker.togetherLion.domain.good.entity.Good;
import CodeMaker.togetherLion.domain.good.repository.GoodRepository;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final PostRepository postRepository;
    private final SessionUtil sessionUtil;

    public GoodRes createGood(GoodReq goodReq) {
        User user = userRepository.findById(goodReq.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음!"));

        Post post = postRepository.findByPostId(goodReq.postId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));

        // 이미 같은 유저와 포스트에 대한 좋아요가 있는지 확인
        if (goodRepository.findByUserIdAndPostId(goodReq.userId(), goodReq.postId()).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        // Good 객체 생성 시 likeCheck 값을 여기서 직접 true로 설정
        Good good = Good.builder()
                .user(user)
                .post(post)
                .likeCheck(true) // likeCheck 값을 true로 설정
                .build();

        good = goodRepository.save(good);

        return GoodRes.fromEntity(good);
    }


    @Transactional
    public void deleteGood(int userId, int postId) {
        Good good = goodRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저와 포스트에 해당하는 좋아요가 존재하지 않습니다."));
        goodRepository.delete(good);
    }

    public List<Post> findLikedPostsByUserId(int userId) {
        return goodRepository.findLikedPostsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean isLiked(int userId, int postId) {
        return goodRepository.findByUserIdAndPostId(userId, postId)
                .map(Good::isLikeCheck)
                .orElse(false);
    }

    public Optional<Good> getGoodByUserIdAndPostId(HttpServletRequest request, int postId) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return goodRepository.findByUserIdAndPostId(userId, postId);
    }

    public Boolean isPostLikedByUser(HttpServletRequest request, int postId) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return goodRepository.findLikeCheckByPostIdAndUserId(userId, postId);
    }

}
