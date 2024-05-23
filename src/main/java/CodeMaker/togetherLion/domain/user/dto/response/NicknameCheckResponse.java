package CodeMaker.togetherLion.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NicknameCheckResponse {

    private boolean nicknameCheck;
}
