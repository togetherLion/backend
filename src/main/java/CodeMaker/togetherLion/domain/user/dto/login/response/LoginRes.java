package CodeMaker.togetherLion.domain.user.dto.login.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginRes {

    private String loginId;
    private int userId;
}
