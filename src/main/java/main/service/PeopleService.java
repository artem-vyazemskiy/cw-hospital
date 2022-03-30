package main.service;

import java.util.List;

import main.entity.People;
import main.entity.request.PeopleRequest;
import main.exception.DiagnosisNotExistsException;
import main.exception.PeopleNotExistsException;
import main.exception.WardNotExistsException;

public interface PeopleService {

    boolean exists(long id);
    List<People> listPeoples();
    List<People> listPeoplesWithDiagnosis(long diagnosisId);
    People findPeople(long id) throws PeopleNotExistsException;
    People addPeople(PeopleRequest peopleRequest) throws WardNotExistsException, DiagnosisNotExistsException;
    void updatePeople(People people, PeopleRequest peopleRequest)
            throws WardNotExistsException, DiagnosisNotExistsException;
    boolean deletePeople(long id);

}
