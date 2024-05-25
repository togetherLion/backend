package CodeMaker.togetherLion.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChangeInfoResponse {

    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String userAddress;
}
