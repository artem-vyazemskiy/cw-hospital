package main.service;

import java.util.List;

import main.entity.Diagnosis;
import main.entity.request.DiagnosisRequest;
import main.exception.DiagnosisIsUsedException;
import main.exception.DiagnosisNotExistsException;
import org.springframework.data.util.Pair;

public interface DiagnosisService {
    boolean exists(long id);
    List<Diagnosis> listDiagnosis();
    Diagnosis findDiagnosis(long id) throws DiagnosisNotExistsException;
    Pair<Diagnosis, Boolean> addDiagnosis(DiagnosisRequest diagnosisRequest);
    boolean deleteDiagnosis(long id) throws DiagnosisIsUsedException;

}
