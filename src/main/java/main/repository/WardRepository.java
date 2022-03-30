package main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import main.entity.Ward;

@Repository
public interface WardRepository extends CrudRepository<Ward, Long> {

    Optional<Ward> findByName(String name);

    @Query("select distinct ward from People people join people.ward ward join people.diagnosis diagnosis " +
            "where diagnosis.id=:diagnosisId")
    List<Ward> findByDiagnosisId(long diagnosisId);

    @Transactional
    @Modifying
    @Query("update Ward ward set ward.name=:name where ward.id=:wardId")
    int updateName(long wardId, String name);

    @Transactional
    @Modifying
    @Query("update Ward ward set ward.maxCount=:maxCount where ward.id=:wardId")
    int updateMaxCount(long wardId, int maxCount);

}
