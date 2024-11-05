package com.jnu.festival.domain.partner.repository;

import com.jnu.festival.domain.partner.entity.Partner;
import com.jnu.festival.domain.partner.entity.PartnerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerImageRepository extends JpaRepository<PartnerImage,Long> {
    @Query("select pi from PartnerImage as pi where pi.partner =:partner")
    List<PartnerImage> findAllByPartner(Partner partner);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from PartnerImage as pi where pi.partner = :partner")
    void deleteAllByPartner(Partner partner);
}