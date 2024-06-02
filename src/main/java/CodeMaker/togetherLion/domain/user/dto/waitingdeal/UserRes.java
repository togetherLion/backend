package CodeMaker.togetherLion.domain.user.dto.waitingdeal;

import CodeMaker.togetherLion.domain.user.entity.User;

public record UserRes (
        int userId,
        String loginId,
        String name,
        String nickname,
        String phone,
        String account,
        String profilePicture

) {
    public static UserRes fromEntity(User user) {
        return new UserRes(user.getUserId(), user.getLoginId(), user.getName(), user.getNickname(), user.getPhone(), user.getAccount(), user.getProfilePicture());
    }
}