package CodeMaker.togetherLion.domain.post.repository;

import CodeMaker.togetherLion.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
