package com.jnu.festival.domain.zone.Controller;

import com.jnu.festival.domain.zone.DTO.response.ZoneListDto;
import com.jnu.festival.domain.zone.Service.ZoneService;
import com.jnu.festival.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zones")
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping(value = "")
    public ResponseEntity<?> readZoneList(@RequestParam(required = false) String location) {
        List<ZoneListDto> response = zoneService.readZoneList(location);
        return ResponseEntity.ok(ResponseDto.ok(response));
    }

}
