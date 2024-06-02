package CodeMaker.togetherLion.domain.waitingdeal.dto;

import CodeMaker.togetherLion.domain.user.dto.waitingdeal.UserRes;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WaitingDealInfo {
    private List<UserRes> users;
    private List<WaitingDealRes> waitingDealRes;
}