package CodeMaker.togetherLion.domain.waitingdeal.controller;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.service.WaitingDealService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/waitingdeal")
public class WaitingDealController {

    private final WaitingDealService waitingDealService;

    @PostMapping("")
    public ResponseEntity<WaitingDealRes> createWaitingDeal(@RequestBody WaitingDealReq waitingDealReq, HttpServletRequest request) {
        WaitingDeal waitingDeal = waitingDealService.createWaitingDeal(waitingDealReq, request);
        WaitingDealRes waitingDealRes = WaitingDealRes.fromEntity(waitingDeal);
        return ResponseEntity.ok(waitingDealRes);
    }


}
