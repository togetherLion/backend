package CodeMaker.togetherLion.domain.user.dto.userInfo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChangeInfoRes {

    private String name;
    private String nickname;
    private String phone;
    private String userAddress;
    private String account;
}
