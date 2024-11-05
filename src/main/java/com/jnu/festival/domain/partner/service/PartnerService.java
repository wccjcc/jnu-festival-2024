package com.jnu.festival.domain.partner.Service;

import com.jnu.festival.domain.bookmark.repository.PartnerBookmarkRepository;
import com.jnu.festival.domain.partner.DTO.PartnerDto;
import com.jnu.festival.domain.partner.DTO.PartnerListDto;
import com.jnu.festival.domain.partner.entity.Partner;
import com.jnu.festival.domain.partner.entity.PartnerImage;
import com.jnu.festival.domain.partner.Repository.PartnerImageRepository;
import com.jnu.festival.domain.partner.Repository.PartnerRepository;
import com.jnu.festival.domain.user.entity.User;
import com.jnu.festival.domain.user.repository.UserRepository;
import com.jnu.festival.global.error.ErrorCode;
import com.jnu.festival.global.error.exception.BusinessException;
import com.jnu.festival.global.security.auth.UserDetailsImpl;
import com.jnu.festival.global.util.LocalDateTimeConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final PartnerBookmarkRepository partnerBookmarkRepository;
    private final PartnerImageRepository partnerImageRepository;

    //파트너 전체 조회
    public List<PartnerListDto> readPartnerList(UserDetailsImpl userDetails) {
        List<Partner> partners = partnerRepository.findAll();
        if (userDetails != null) {
            User user = userRepository.findByNickname(userDetails.getUsername())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
            List<Long> partnerBookmarkIds = partnerBookmarkRepository.findAllByUserAndIsDeleted(user).stream()
                    .map(Partner::getId)
                    .toList();

            return partners.stream()
                    .map(partner -> PartnerListDto.builder()
                            .id(partner.getId())
                            .name(partner.getName())
                            .description(partner.getDescription())
                            .bookmark(partnerBookmarkIds.contains(partner.getId()))
                            .createdAt(LocalDateTimeConvertUtil.convertUtcToLocalDateTIme(partner.getCreatedAt()))
                            .build())
                    .toList();
        }
        return partners.stream()
                .map(partner -> PartnerListDto.builder()
                        .id(partner.getId())
                        .name(partner.getName())
                        .description(partner.getDescription())
                        .bookmark(false)
                        .createdAt(LocalDateTimeConvertUtil.convertUtcToLocalDateTIme(partner.getCreatedAt()))
                        .build())
                .toList();
    }

    //파트너 상세 조회
    public PartnerDto readPartner(Long partnerId, UserDetailsImpl userDetails) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND_PARTNER));
        List<String> partnerImages = partnerImageRepository.findAllByPartner(partner).stream()
                .map(PartnerImage::getUrl)
                .toList();

        if (userDetails != null) {
            User user = userRepository.findByNickname(userDetails.getUsername())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
            Long partnerBookmarkId = partnerBookmarkRepository.findByUserAndPartnerAndIsDeleted(user, partner)
                    .map(Partner::getId).orElse(null);
            return PartnerDto.builder()
                    .id(partner.getId())
                    .name(partner.getName())
                    .description(partner.getDescription())
                    .images(partnerImages)
                    .location(partner.getLocation())
                    .startDate(partner.getStartDate())
                    .endDate(partner.getEndDate())
                    .bookmark(partner.getId().equals(partnerBookmarkId))
                    .createdAt(LocalDateTimeConvertUtil.convertUtcToLocalDateTIme(partner.getCreatedAt()))
                    .build();
        }
        return PartnerDto.builder()
                .id(partner.getId())
                .name(partner.getName())
                .description(partner.getDescription())
                .images(partnerImages)
                .location(partner.getLocation())
                .startDate(partner.getStartDate())
                .endDate(partner.getEndDate())
                .bookmark(false)
                .createdAt(LocalDateTimeConvertUtil.convertUtcToLocalDateTIme(partner.getCreatedAt()))
                .build();

    }
}
