package CodeMaker.togetherLion.domain.user.dto.userInfo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChangePwRes {

    private String password;
}
