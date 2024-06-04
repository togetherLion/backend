package CodeMaker.togetherLion.domain.place.dto;

import CodeMaker.togetherLion.domain.place.model.PlaceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PlaceReq {

    private String placeLat;
    private String placeLong;
    private String placeName;
    private PlaceType placeType;
}
