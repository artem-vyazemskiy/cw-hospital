package main.service;

import java.util.List;

import main.entity.Person;
import main.entity.request.PersonRequest;
import main.exception.DiagnosisNotExistsException;
import main.exception.PersonNotExistsException;
import main.exception.WardNotExistsException;
import org.springframework.data.util.Pair;

public interface PersonService {

    boolean exists(long id);
    List<Person> listPeople();
    List<Person> listPeopleWithDiagnosis(long diagnosisId);
    Person findPerson(long id) throws PersonNotExistsException;
    Pair<Person, Boolean> addPerson(PersonRequest personRequest) throws WardNotExistsException, DiagnosisNotExistsException;
    boolean updatePerson(long id, PersonRequest personRequest)
            throws PersonNotExistsException, WardNotExistsException, DiagnosisNotExistsException;
    boolean deletePerson(long id);

}
