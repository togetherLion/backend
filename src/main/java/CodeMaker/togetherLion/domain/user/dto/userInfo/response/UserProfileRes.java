package CodeMaker.togetherLion.domain.user.dto.userInfo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserProfileRes {

    private String nickname;
    private String profilePicture;
    private String profileIntro;
    private boolean isMyProfile;
}
