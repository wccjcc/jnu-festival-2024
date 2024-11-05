package com.jnu.festival.domain.partner.controller;


import com.jnu.festival.domain.partner.DTO.PartnerDto;
import com.jnu.festival.domain.partner.DTO.PartnerListDto;
import com.jnu.festival.domain.partner.Service.PartnerService;
import com.jnu.festival.global.common.ResponseDto;
import com.jnu.festival.global.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partners")
public class PartnerController {
    private final PartnerService partnerService;

    //파트너 목록 조회
    @GetMapping(value = "")
    public ResponseEntity<?> readPartnerList(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        List<PartnerListDto> response = partnerService.readPartnerList(userDetails);
        return ResponseEntity.ok(ResponseDto.ok(response));
    }

    //파트너 상세 조회
    @GetMapping("/{partnerId}")
    public ResponseEntity<?> readPartner(@PathVariable Long partnerId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        PartnerDto response = partnerService.readPartner(partnerId, userDetails);
        return ResponseEntity.ok(ResponseDto.ok(response));
    }


}
