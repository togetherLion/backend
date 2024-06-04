package CodeMaker.togetherLion.domain.place.entity;

import CodeMaker.togetherLion.domain.place.dto.PlaceDto;
import CodeMaker.togetherLion.domain.place.dto.PlaceRes;
import CodeMaker.togetherLion.domain.place.model.PlaceType;
import CodeMaker.togetherLion.domain.region.entity.Region;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Data
@SqlResultSetMapping(
        name = "place",
        classes = {
                @ConstructorResult(
                        targetClass = PlaceDto.class,
                        columns = {
                                @ColumnResult(name = "place_id", type = int.class),
                                @ColumnResult(name = "place_lat", type = String.class),
                                @ColumnResult(name = "place_long", type = String.class),
                                @ColumnResult(name = "place_name", type = String.class),
                                @ColumnResult(name = "place_type", type = String.class),
                                //@ColumnResult(name = "region_id", type = int.class)
                        }
                )
        }
)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    @Column
    private String placeLat; // 위도

    @Column
    private String placeLong; // 경도

    @Column
    private String placeName;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @ManyToOne
    @JoinColumn(name = "regionId") // 이는 데이터베이스의 실제 외래키 컬럼명을 지정합니다.
    private Region region;

}
