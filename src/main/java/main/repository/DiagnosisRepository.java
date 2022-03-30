package main.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import main.entity.Diagnosis;

import java.util.Optional;

@Repository
public interface DiagnosisRepository extends CrudRepository<Diagnosis, Long> {

    Optional<Diagnosis> findByName(String name);

}
