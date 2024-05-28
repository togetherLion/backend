package CodeMaker.togetherLion.domain.user.dto.userInfo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ModifyProfileRes {

    private String nickname;
    private String profilePicture;
    private String profileIntro;
}
