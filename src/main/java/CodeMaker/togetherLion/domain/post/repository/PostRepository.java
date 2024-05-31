package CodeMaker.togetherLion.domain.post.repository;

import CodeMaker.togetherLion.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // SELECT * FROM togetherlion.post
    //WHERE product_name LIKE '%칼%';

    // 키워드 검색 - 근데 전체 게시글 대상이라 안쓸듯
//    @Query(value = "SELECT p FROM Post p " +
//            "WHERE p.productName LIKE %:searchText% " +
//            "ORDER BY p.uploadDate DESC ")
//    List<Post> searchBySearchText(@Param("searchText") String searchText);

    // 사용자가 속한 지역 게시글 조회
    @Query("SELECT p " +
            "FROM Post AS p " +
            "INNER JOIN User AS u ON p.user.userId = u.userId " +
            "WHERE u.region = (" +
            "   SELECT region " +
            "   FROM User " +
            "   WHERE userId = :userId) " +
            "ORDER BY p.uploadDate DESC ")
    List<Post> findPostByRegion(@Param("userId") int userId);

    // (지역 내의) 키워드 검색 - 최신순
    @Query("SELECT p " +
            "FROM Post AS p " +
            "INNER JOIN User AS u ON p.user.userId = u.userId " +
            "WHERE u.region = (" +
            "   SELECT region " +
            "   FROM User " +
            "   WHERE userId = :userId) " +
            "AND p.productName LIKE %:searchText% " +
            "ORDER BY p.uploadDate DESC ")
    List<Post> searchPostByRegion(@Param("userId") int userId, @Param("searchText") String searchText);

    // 조회 옵션 - 저가순
    @Query("SELECT p " +
            "FROM Post AS p " +
            "INNER JOIN User AS u ON p.user.userId = u.userId " +
            "WHERE u.region = (" +
            "   SELECT region " +
            "   FROM User " +
            "   WHERE userId = :userId) " +
            "AND p.productName LIKE %:searchText% " +
            "ORDER BY p.price ")
    List<Post> searchPostLowCost(@Param("userId") int userId, @Param("searchText") String searchText);

    // 조회 옵션 - 고가순
    @Query("SELECT p " +
            "FROM Post AS p " +
            "INNER JOIN User AS u ON p.user.userId = u.userId " +
            "WHERE u.region = (" +
            "   SELECT region " +
            "   FROM User " +
            "   WHERE userId = :userId) " +
            "AND p.productName LIKE %:searchText% " +
            "ORDER BY p.price DESC")
    List<Post> searchPostHighCost(@Param("userId") int userId, @Param("searchText") String searchText);


    // 조회 옵션 - 가격대 설정
    @Query("SELECT p " +
            "FROM Post AS p " +
            "INNER JOIN User AS u ON p.user.userId = u.userId " +
            "WHERE u.region = (" +
            "   SELECT region " +
            "   FROM User " +
            "   WHERE userId = :userId) " +
            "AND p.productName LIKE %:searchText% " +
            "AND p.price >= :lowPrice " +
            "AND p.price <= :highPrice " +
            "ORDER BY p.price DESC")
    List<Post> searchPostPriceZone(@Param("userId") int userId, @Param("searchText") String searchText,
                                  @Param("lowPrice") int lowPrice, @Param("highPrice") int highPrice);
}
