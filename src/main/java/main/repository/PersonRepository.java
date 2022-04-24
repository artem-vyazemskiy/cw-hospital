package main.repository;

import java.util.List;

import main.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import main.entity.Ward;
import main.entity.Diagnosis;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByDiagnosisId(long diagnosisId);
    List<Person> findByWardId(long wardId);

    @Transactional
    @Modifying
    @Query("update Person person " + "set person.fullName.firstName=:firstName, " +
            "person.fullName.lastName=:lastName, person.fullName.fatherName=:fatherName " +
            "where person.id=:personId")
    int updateFullName(long personId, String firstName, String lastName, String fatherName);

    @Transactional
    @Modifying
    @Query("update Person person set person.ward=:ward where person.id=:personId")
    int updateWard(long personId, Ward ward);

    @Transactional
    @Modifying
    @Query("update Person person set person.diagnosis=:diagnosis where person.id=:personId")
    int updateDiagnosis(long personId, Diagnosis diagnosis);

}
