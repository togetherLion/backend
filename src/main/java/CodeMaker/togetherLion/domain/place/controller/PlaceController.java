package CodeMaker.togetherLion.domain.place.controller;

import CodeMaker.togetherLion.domain.place.dto.PlaceReq;
import CodeMaker.togetherLion.domain.place.dto.PlaceRes;
import CodeMaker.togetherLion.domain.place.entity.Place;
import CodeMaker.togetherLion.domain.place.repository.PlaceRepository;
import CodeMaker.togetherLion.domain.region.entity.Region;
import CodeMaker.togetherLion.domain.region.repository.RegionRepository;
import CodeMaker.togetherLion.domain.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final RegionRepository regionRepository;
    private final LoginService loginService;
    private final PlaceRepository placeRepository;

    // 주소 넣는 용
    @PostMapping("")
    public List<PlaceRes> inputPlace(@RequestBody List<PlaceReq> params) {
        List<PlaceRes> list = new ArrayList<>();

        for(PlaceReq placeReq : params) {
            int regionId = loginService.findAddress(placeReq.getPlaceLat(), placeReq.getPlaceLong());
            Region region = regionRepository.findById(regionId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 지역 id 입니다."));

            Place place = Place.builder()
                    .placeLat(placeReq.getPlaceLat())
                    .placeLong(placeReq.getPlaceLong())
                    .placeType(placeReq.getPlaceType())
                    .placeName(placeReq.getPlaceName())
                    .region(region)
                    .build();
            placeRepository.save(place);

            list.add(new PlaceRes(place.getPlaceId(), place.getPlaceLat(), place.getPlaceLong(),
                    place.getPlaceName(), place.getPlaceType(), place.getRegion().getRegionId()));
        }

        return list;
    }
}
