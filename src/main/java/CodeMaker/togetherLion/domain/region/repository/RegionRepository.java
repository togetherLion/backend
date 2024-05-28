package CodeMaker.togetherLion.domain.region.repository;

import CodeMaker.togetherLion.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    boolean existsByCity(String city);

    boolean existsByDistrict(String district);

    boolean existsByTownName(String townName);

    @Query(value = "SELECT regionId FROM Region  " +
            "WHERE city = :city " +
            "AND district = :district " +
            "AND townName = :townName")
    int getRegionId(@Param("city") String city,
                    @Param("district") String district,
                    @Param("townName") String townName);

}
