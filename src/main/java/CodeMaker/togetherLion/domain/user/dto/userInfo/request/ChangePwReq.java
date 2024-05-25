package CodeMaker.togetherLion.domain.user.dto.userInfo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
public class ChangePwReq {

    private String password;
}
