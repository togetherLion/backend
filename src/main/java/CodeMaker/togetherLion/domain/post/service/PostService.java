package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(Integer userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음!"));

        post.setUser(user);
        return postRepository.save(post);
    }

    public Post getPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));
        return post;
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

//    public Post updatePost(Integer postId, Post post) {
//        Post foundPost = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));
//
//        foundPost.update(post.getTitle(), post.getContent());
//        return foundPost;
//    }

}
