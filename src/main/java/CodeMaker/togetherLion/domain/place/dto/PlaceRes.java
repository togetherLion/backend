package CodeMaker.togetherLion.domain.place.dto;

import CodeMaker.togetherLion.domain.place.model.PlaceType;
import lombok.*;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceRes {

    private int placeId;
    private String placeLat;
    private String placeLong;
    private String placeName;
    private PlaceType placeType;
    private int regionId;

//    public PlaceRes(int placeId, String placeLat, String placeLong, String placeName,
//                    PlaceType placeType, int regionId) {
//        this.placeId = placeId;
//        this.placeLat = placeLat;
//        this.placeLong = placeLong;
//        this.placeName = placeName;
//        this.placeType = placeType;
//        this.regionId = regionId;
//    }

}
