package net.branium.location;

import jakarta.transaction.Transactional;
import net.branium.common.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, String>, PagingAndSortingRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    @Deprecated
    List<Location> findAllUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    Page<Location> findAllUnTrashed(Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.code = :code AND l.trashed = false")
    Optional<Location> findByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("UPDATE Location l SET l.trashed = true WHERE l.code = ?1")
    void deleteByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.countryCode = ?1 AND l.cityName = ?2 AND l.trashed = false")
    Optional<Location> findByCountryCodeAndCityName(String countryCode, String cityName);
}
