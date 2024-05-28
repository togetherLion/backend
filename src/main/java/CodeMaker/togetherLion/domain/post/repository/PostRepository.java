package CodeMaker.togetherLion.domain.post.repository;

import CodeMaker.togetherLion.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // SELECT * FROM togetherlion.post
    //WHERE product_name LIKE '%칼%';

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.productName LIKE %:searchText% ")
    List<Post> searchBySearchText(@Param("searchText") String searchText);


}
