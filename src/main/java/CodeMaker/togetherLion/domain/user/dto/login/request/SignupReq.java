package CodeMaker.togetherLion.domain.user.dto.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupReq {

    private String loginId;
    private String password;

    private String name;
    private String nickname;

    private String phone;

    private String userAddress;
    private String userLat;
    private String userLong;
}
