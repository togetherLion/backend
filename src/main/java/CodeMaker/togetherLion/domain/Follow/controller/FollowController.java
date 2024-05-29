package CodeMaker.togetherLion.domain.Follow.controller;

import CodeMaker.togetherLion.domain.Follow.dto.request.FollowReq;
import CodeMaker.togetherLion.domain.Follow.dto.request.UnfollowReq;
import CodeMaker.togetherLion.domain.Follow.dto.response.FollowRes;
import CodeMaker.togetherLion.domain.Follow.dto.response.UnfollowRes;
import CodeMaker.togetherLion.domain.Follow.service.FollowService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final SessionUtil sessionUtil;

    // 팔로우
    @PostMapping("")
    public FollowRes follow(@RequestBody FollowReq followReq, HttpServletRequest request) {
        int followingUserId = sessionUtil.getUserIdFromSession(request);
        return followService.follow(followReq, followingUserId);
    }

    // 언팔로우
    @PostMapping("/unfollow")
    public UnfollowRes unfollow(@RequestBody UnfollowReq unfollowReq, HttpServletRequest request) {
        int unfollowingUserId = sessionUtil.getUserIdFromSession(request);
        return followService.unfollow(unfollowReq, unfollowingUserId);
    }
}
