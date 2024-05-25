package CodeMaker.togetherLion.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeInfoRequest {

    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String userAddress;
}
