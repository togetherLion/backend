package CodeMaker.togetherLion.domain.usersearch.dto;

import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;

import java.time.LocalDateTime;

public record UserSearchRes (

        int userSearchId,
        LocalDateTime searchDate,
        String searchText,
        int userId
) {

    // 파라미터 여러 개
    public static UserSearchRes of(int userSearchId, LocalDateTime searchDate, String searchText, int userId) {
        return new UserSearchRes(userSearchId, searchDate, searchText, userId);
    }

    // 단일 파라미터
    public static UserSearchRes fromEntity(UserSearch userSearch) {
        return new UserSearchRes(userSearch.getUserSearchId(),
                userSearch.getSearchDate(),
                userSearch.getSearchText(),
                userSearch.getUser().getUserId());
    }
}
