package CodeMaker.togetherLion.domain.complain.controller;

import CodeMaker.togetherLion.domain.complain.dto.ComplainReq;
import CodeMaker.togetherLion.domain.complain.dto.ComplainRes;
import CodeMaker.togetherLion.domain.complain.entity.Complain;
import CodeMaker.togetherLion.domain.complain.service.ComplainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/complain")
@RequiredArgsConstructor
public class ComplainController {

    private final ComplainService complainService;

    // 신고하기
    @PostMapping("")
    public ResponseEntity<ComplainRes> creatComplain(@RequestBody ComplainReq complainReq, HttpServletRequest request) {
        Complain createComplain = complainService.creatComplain(complainReq, request);
        ComplainRes complainRes = ComplainRes.fromEntity(createComplain);
        return ResponseEntity.ok(complainRes);
    }
}
