package CodeMaker.togetherLion.domain.user.dto.login.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FindAddressRes {

    private String userAddress; // 도로명 주소?
    private String userRegion1Depth; // 경기도
    private String userRegion2Depth; // 성남시 분당구
    private String userRegion3Depth; // 삼평동
}
