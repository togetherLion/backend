package CodeMaker.togetherLion.domain.user.dto.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginReq {

    private String loginId;
    private String password;
}
