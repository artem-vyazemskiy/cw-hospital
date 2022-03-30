package main.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entity.Diagnosis;
import main.entity.Ward;
import main.entity.People;
import main.entity.request.PeopleRequest;
import main.exception.DiagnosisNotExistsException;
import main.exception.WardNotExistsException;
import main.exception.PeopleNotExistsException;
import main.repository.PeopleRepository;

@Service
public class PeopleServiceImpl implements PeopleService {

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    WardService wardService;

    @Autowired
    DiagnosisService diagnosisService;

    @Override
    public boolean exists(long id) {
        return peopleRepository.existsById(id);
    }

    @Override
    public List<People> listPeoples() {
        return (ArrayList<People>) peopleRepository.findAll();
    }

    @Override
    public List<People> listPeoplesWithDiagnosis(long diagnosisId) {
        return peopleRepository.findByDiagnosisId(diagnosisId);
    }

    @Override
    public People findPeople(long id) throws PeopleNotExistsException {
        Optional<People> optionalPeople = peopleRepository.findById(id);
        return optionalPeople.orElseThrow(() -> new PeopleNotExistsException("Пациент не найден"));
    }

    @Override
    public People addPeople(PeopleRequest peopleRequest) throws WardNotExistsException, DiagnosisNotExistsException {
        People people = new People();
        people.setFullName(peopleRequest.getFullName());
        people.setWard(wardService.findWard(peopleRequest.getWardId()));
        people.setDiagnosis(diagnosisService.findDiagnosis(peopleRequest.getDiagnosisId()));
        return peopleRepository.save(people);
    }

    @Override
    public void updatePeople(People people, PeopleRequest peopleRequest)
            throws WardNotExistsException, DiagnosisNotExistsException {
        if (!people.getFullName().equals(peopleRequest.getFullName())) {
            people.setFullName(peopleRequest.getFullName());
            peopleRepository.updateFullName(people.getId(), peopleRequest.getFullName());
        }
        if (people.getWard().getId() != peopleRequest.getWardId()) {
            Ward ward = wardService.findWard(peopleRequest.getWardId());
            ward.getPeoples().add(people);
            people.setWard(ward);
            peopleRepository.updateWard(people.getId(), ward);
        }
        if (people.getDiagnosis().getId() != peopleRequest.getDiagnosisId()) {
            Diagnosis diagnosis = diagnosisService.findDiagnosis(peopleRequest.getDiagnosisId());
            diagnosis.getPeoples().add(people);
            people.setDiagnosis(diagnosis);
            peopleRepository.updateDiagnosis(people.getId(), diagnosis);
        }
    }

    @Override
    public boolean deletePeople(long id) {
        if (!peopleRepository.existsById(id)) {
            return false;
        }
        peopleRepository.deleteById(id);
        return true;
    }

}
