package CodeMaker.togetherLion.domain.user.dto.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindAddressReq {

    private String userLat; // 위도
    private String userLong; // 경도
}
