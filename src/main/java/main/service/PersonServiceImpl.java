package main.service;

import java.util.*;

import main.entity.Person;
import main.entity.component.FullName;
import main.exception.WardIsFullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import main.entity.Diagnosis;
import main.entity.Ward;
import main.entity.request.PersonRequest;
import main.exception.DiagnosisNotExistsException;
import main.exception.WardNotExistsException;
import main.exception.PersonNotExistsException;
import main.repository.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    WardService wardService;

    @Autowired
    DiagnosisService diagnosisService;

    @Override
    public boolean exists(long id) {
        return personRepository.existsById(id);
    }

    @Override
    public List<Person> listPeople() {
        return (ArrayList<Person>) personRepository.findAll();
    }

    @Override
    public List<Person> listPeopleWithDiagnosis(long diagnosisId) {
        return personRepository.findByDiagnosisId(diagnosisId);
    }

    @Override
    public Person findPerson(long id) throws PersonNotExistsException {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElseThrow(() -> new PersonNotExistsException("Пациент не найден"));
    }

    @Override
    public List<Person> findByLastName(String lastName) {
        return personRepository.findByFullName_LastName(lastName);
    }

    @Override
    public Pair<Person, Boolean> addPerson(PersonRequest personRequest)
            throws WardNotExistsException, DiagnosisNotExistsException, WardIsFullException {
        Ward ward = wardService.findWard(personRequest.getWardId());
        if (ward.getPeople().size() == ward.getMaxCount()) {
            throw new WardIsFullException("Палата заполнена");
        }
        Person person = new Person();
        person.setFullName(personRequest.getFullName());
        person.setWard(ward);
        person.setDiagnosis(diagnosisService.findDiagnosis(personRequest.getDiagnosisId()));
        return Pair.of(personRepository.save(person), true);
    }

    @Override
    public boolean updatePerson(long id, PersonRequest personRequest)
            throws PersonNotExistsException, WardNotExistsException, DiagnosisNotExistsException, WardIsFullException {
        Person person = this.findPerson(id);
        boolean updated = false;
        if (!person.getFullName().equals(personRequest.getFullName())) {
            FullName fullName = personRequest.getFullName();
            personRepository.updateFullName(person.getId(),
                    fullName.getFirstName(), fullName.getLastName(), fullName.getFatherName());
            updated = true;
        }
        if (person.getWard().getId() != personRequest.getWardId()) {
            Ward ward = wardService.findWard(personRequest.getWardId());
            if (ward.getPeople().size() == ward.getMaxCount()) {
                throw new WardIsFullException("Палата заполнена");
            }
            personRepository.updateWard(person.getId(), ward);
            updated = true;
        }
        if (person.getDiagnosis().getId() != personRequest.getDiagnosisId()) {
            Diagnosis diagnosis = diagnosisService.findDiagnosis(personRequest.getDiagnosisId());
            personRepository.updateDiagnosis(person.getId(), diagnosis);
            updated = true;
        }
        return updated;
    }

    @Override
    public boolean deletePerson(long id) {
        if (!personRepository.existsById(id)) {
            return false;
        }
        personRepository.deleteById(id);
        return true;
    }

}
