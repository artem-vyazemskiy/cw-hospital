package main.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import main.entity.People;
import main.entity.Ward;
import main.entity.Diagnosis;
import main.entity.component.FullName;

@Repository
public interface PeopleRepository extends CrudRepository<People, Long> {

    List<People> findByDiagnosisId(long diagnosisId);
    List<People> findByWardId(long wardId);

    @Transactional
    @Modifying
    @Query("update People people set people.fullName=:fullName where people.id=:peopleId")
    int updateFullName(long peopleId, FullName fullName);

    @Transactional
    @Modifying
    @Query("update People people set people.ward=:ward where people.id=:peopleId")
    int updateWard(long peopleId, Ward ward);

    @Transactional
    @Modifying
    @Query("update People people set people.diagnosis=:diagnosis where people.id=:peopleId")
    int updateDiagnosis(long peopleId, Diagnosis diagnosis);

}
