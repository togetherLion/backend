package CodeMaker.togetherLion.domain.place.repository;

import CodeMaker.togetherLion.domain.place.dto.PlaceDto;
import CodeMaker.togetherLion.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    // 거래 장소 추천
    @Query(value = "SELECT new CodeMaker.togetherLion.domain.place.dto.PlaceDto(" +
            "p.place_id AS placeId, " +
            "p.place_lat AS placeLat, " +
            "p.place_long AS placeLong, " +
            "p.place_type AS placeType, " +
            "p.place_name AS placeName, " +
            "p.region_id AS regionId, " +
            "(6371 * ACOS(COS(RADIANS(?1)) * COS(RADIANS(p.place_lat)) * COS(RADIANS(p.place_long) - RADIANS(?2)) + SIN(RADIANS(?1)) * SIN(RADIANS(p.place_lat)))) AS distance) " +
            "FROM togetherlion.place p " +
            "ORDER BY distance " +
            "LIMIT 5", nativeQuery = true)
    List<PlaceDto> findClosestPlaces(double lat, double lon);

//    @Query(value = "SELECT place_id, place_lat, place_long, place_type, place_name, " +
//            "(6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(place_lat)) * COS(RADIANS(place_long) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(place_lat)))) AS distance " +
//            "FROM togetherlion.place ORDER BY distance LIMIT 5", nativeQuery = true)
    @Query(value = "SELECT p.place_id, p.place_lat, p.place_long, p.place_type, p.place_name, (6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(p.place_lat)) * COS(RADIANS(p.place_long) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(p.place_lat)))) AS distance FROM place p ORDER BY distance LIMIT 5", nativeQuery = true)
    List<Object[]> findPlacesNearby(@Param("lat") double lat, @Param("lon") double lon);
}
