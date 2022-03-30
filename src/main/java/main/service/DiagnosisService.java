package main.service;

import java.util.List;

import main.entity.Diagnosis;
import main.entity.request.DiagnosisRequest;
import main.exception.DiagnosisIsUsedException;
import main.exception.DiagnosisNotExistsException;

public interface DiagnosisService {
    boolean exists(long id);
    List<Diagnosis> listDiagnosis();
    Diagnosis findDiagnosis(long id) throws DiagnosisNotExistsException;
    Diagnosis addDiagnosis(DiagnosisRequest diagnosisRequest);
    void deleteDiagnosis(long id) throws DiagnosisNotExistsException, DiagnosisIsUsedException;

}
