package com.jnu.festival.api.domain.booth.controller;


import com.jnu.festival.domain.booth.controller.BoothController;
import com.jnu.festival.domain.booth.dto.BoothDto;
import com.jnu.festival.domain.booth.dto.BoothListDto;
import com.jnu.festival.domain.booth.repository.BoothRepository;
import com.jnu.festival.domain.booth.service.BoothService;
import com.jnu.festival.global.util.ResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.argThat;


@ExtendWith(MockitoExtension.class)
class BoothControllerTest {

    @InjectMocks
    private BoothController boothController;
    @Mock
    private BoothService boothService;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private BoothRepository boothRepository;

    private List<BoothListDto> boothList;

    @BeforeEach
    void setup() {
        boothList = List.of(
                new BoothListDto(1L, "부스이름1", "location1", 1, LocalDate.now(), LocalDate.now().plusDays(1),
                        LocalTime.of(9, 0), LocalTime.of(18, 0), false, 10L),
                new BoothListDto(2L, "부스2", "location2", 2, LocalDate.now(), LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0), LocalTime.of(20, 0), true, 15L)
        );
    }


    @Test
    @DisplayName("부스 목록 조회 - location, period, category 조건 만족")
    @WithMockUser
    void readBoothListWithValidParams() throws Exception {
        // 조건 >> query string을 제  대로 작성했는지?
        when(boothService.readBoothList(
                argThat(location -> location == null || List.of("stadium", "square-518", "backgate-street").contains(location)),
                argThat(period -> period == null || List.of("daytime", "nighttime", "alltime").contains(period)),
                argThat(category -> category == null || List.of("food", "flea-market", "promotion", "experience", "etc").contains(category)),
                ArgumentMatchers.isNull()
        )).thenReturn(boothList);


        mockMvc.perform(get("/api/v1/booths")
                        .param("location", "a")
                        .param("period", "d")
                        .param("category", "er")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("부스이름1"))
                .andExpect(jsonPath("$.data[1].name").value("부스2"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("부스 목록 조회 - 조건 불만족 시 실패")
    @WithMockUser
    void readBoothListWithInvalidParams() throws Exception {
        mockMvc.perform(get("/api/v1/booths")
                        .param("location", "invalid")  // 잘못된 location
                        .param("period", "d")
                        .param("category", "er")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());  // 조건 불만족 시 실패 처리
    }
}
