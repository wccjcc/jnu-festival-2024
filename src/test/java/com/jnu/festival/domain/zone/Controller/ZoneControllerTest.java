package com.jnu.festival.domain.zone.Controller;

import com.jnu.festival.domain.zone.DTO.response.ZoneListDto;
import com.jnu.festival.domain.zone.Entity.Zone;
import com.jnu.festival.domain.zone.Repository.ZoneRepository;
import com.jnu.festival.domain.zone.Service.ZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {

    @InjectMocks
    private ZoneController zoneController;

    @Mock
    private ZoneService zoneService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(zoneController).build();
    }
    @DisplayName("zone 목록 조회 성공")
    @Test
    void readZoneListSuccess() throws Exception {
        //given
        String location = "stadium";
        List<ZoneListDto> zones = List.of(
                new ZoneListDto(1L,"메인 무대","메인무대입니다."),
                new ZoneListDto(2L,"서브 무대","서브무대입니다.")
        );

        //when
        when(zoneService.readZoneList(location)).thenReturn(zones);

        //then
        mockMvc.perform(get("/api/v1/zones")
                .param("location",location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @DisplayName("zone 목록 조회 실패 - query string이 일치하는 값이 없음")
    @Test
    void readZoneListInvalidLocation() throws Exception {
        //given
        String invalidLocation = "invalid-location";

        //when
        when(zoneService.readZoneList(invalidLocation)).thenThrow(new IllegalArgumentException("Invalid location"));

        //then
        mockMvc.perform(get("/api/v1/zones")
                .param("location",invalidLocation))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid location"));
    }



}