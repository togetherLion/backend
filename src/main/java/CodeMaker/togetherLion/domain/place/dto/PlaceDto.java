package CodeMaker.togetherLion.domain.place.dto;

import CodeMaker.togetherLion.domain.place.model.PlaceType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceDto {

    private int placeId;
    private String placeLat;
    private String placeLong;
    private String placeName;
    private String placeType;
    private double distance;

}
