package CodeMaker.togetherLion.domain.Follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UnfollowRes {

    private String unfollowedNickname;
    private String unfollowingNickname;
}
