package CodeMaker.togetherLion.domain.usersearch.dto;

import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;

import java.time.LocalDateTime;

public record UserSearchReq (

    //int userSearchId, // 이거 넣는게 맞나?
    LocalDateTime searchDate,
    String searchText,
    User user
    ) {

    public UserSearch toEntity(User user) {

        return UserSearch.builder()
                .searchDate(searchDate)
                .searchText(searchText)
                .build();
    }
}
