package com.jnu.festival.domain.zone.repository;

<<<<<<< HEAD
import com.jnu.festival.domain.common.Location;
import com.jnu.festival.domain.zone.entity.Zone;
=======
import com.jnu.festival.global.common.Location;
>>>>>>> upstream/develop
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone,Long> {
    @Query("select z from Zone as z where (:location is null or z.location = :location)")
    List<Zone> findAllByLocation(Location location);
}
