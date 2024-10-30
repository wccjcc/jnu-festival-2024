package com.jnu.festival.domain.zone.controller;

import com.jnu.festival.domain.common.Location;
import com.jnu.festival.domain.zone.dto.response.ZoneListDto;
import com.jnu.festival.domain.zone.entity.Zone;
import com.jnu.festival.domain.zone.service.ZoneService;
import com.jnu.festival.global.error.ErrorCode;
import com.jnu.festival.global.error.GlobalExceptionHandler;
import com.jnu.festival.global.error.exception.BusinessException;
import com.jnu.festival.global.security.config.SecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(ZoneController.class)
@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {
//    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ZoneService zoneService;

    @InjectMocks
    private ZoneController zoneController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(zoneController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
//    @WithMockUser
    void location에_따른_zone_데이터_부재_시_zone_목록_조회_성공() throws Exception {
        // given
        List<ZoneListDto> zoneListDtos = new ArrayList<>();

        given(zoneService.readZoneList("stadium")).willReturn(zoneListDtos);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/zones")
                .param("location", "stadium"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andDo(print());

    }

    @Test
//    @WithMockUser
    void location에_따른_zone_데이터_존재_시_zone_목록_조회_성공() throws Exception {
        // given
        List<ZoneListDto> response = new ArrayList<>();
        response.add(ZoneListDto.builder()
                .id(1L)
                .name("Main Stadium")
                .description("The main stadium for large events.")
                .build());

        response.add(ZoneListDto.builder()
                .id(2L)
                .name("Sports Field")
                .description("Field for sports activities.")
                .build());

        response.add(ZoneListDto.builder()
                .id(3L)
                .name("VIP Stadium")
                .description("VIP section in the stadium.")
                .build());

        response.add(ZoneListDto.builder()
                .id(4L)
                .name("Festival Stadium")
                .description("Stadium used for festival activities.")
                .build());

        given(zoneService.readZoneList("stadium")).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/zones")
                .param("location", "stadium"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(4))
                .andDo(print());

    }

    @Test
//    @WithMockUser
    void location이_null인_경우_zone_목록_조회_실패() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/zones")
                .param("location", (String) null));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("필수 파라미터가 누락되었습니다."))
                .andDo(print());

    }

    @Test
//    @WithMockUser
    void 유효하지_않은_location인_경우_zone_목록_조회_실패() throws Exception {
        // given
        String invalidLocation = "invalid_location";

        given(zoneService.readZoneList(invalidLocation)).willThrow(new BusinessException(ErrorCode.INVALID_LOCATION));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/zones")
                .param("location", invalidLocation));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 장소입니다."))
                .andDo(print());
    }
}