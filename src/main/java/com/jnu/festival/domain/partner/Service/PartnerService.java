package com.jnu.festival.domain.partner.Service;

import com.jnu.festival.domain.partner.DTO.PartnerDto;
import com.jnu.festival.domain.partner.DTO.PartnerSummaryDto;
import com.jnu.festival.domain.partner.Entity.Partner;
import com.jnu.festival.domain.partner.Repository.PartnerImageRepository;
import com.jnu.festival.domain.partner.Repository.PartnerJPARepository;
import com.jnu.festival.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerJPARepository partnerJPARepository;
    private final PartnerImageRepository partnerImageRepository;

//    @Autowired
//    public PartnerService(PartnerJPARepository partnerJPARepository){
//        this.partnerJPARepository = partnerJPARepository;
//    }
    //파트너 전체 조회
    public List<PartnerSummaryDto> getAllPartners(){
        List<Partner> partners = partnerJPARepository.findAll();
        return partners.stream()
                .map(Partner -> new PartnerSummaryDto(Partner.getId(),Partner.getName(),Partner.getCreatedDate(),Partner.getDescription()))
                .collect(Collectors.toList());

    }

    //파트너 상세 조회
    public Partner getPartnerById(Long id){
        Partner partner = partnerJPARepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Partner not found"));
        return partner;

    }



}
