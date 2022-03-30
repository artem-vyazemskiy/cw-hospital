package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.exception.DiagnosisIsUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entity.Diagnosis;
import main.entity.People;
import main.entity.request.DiagnosisRequest;
import main.exception.DiagnosisNotExistsException;
import main.repository.DiagnosisRepository;
import main.repository.PeopleRepository;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    PeopleRepository peopleRepository;

    @Override
    public boolean exists(long id) {
        return diagnosisRepository.existsById(id);
    }

    @Override
    public List<Diagnosis> listDiagnosis() {
        return (ArrayList<Diagnosis>) diagnosisRepository.findAll();
    }

    @Override
    public Diagnosis findDiagnosis(long id) throws DiagnosisNotExistsException {
        Optional<Diagnosis> optionalDiagnosis = diagnosisRepository.findById(id);
        return optionalDiagnosis.orElseThrow(() -> new DiagnosisNotExistsException("Диагноз не найден"));
    }

    @Override
    public Diagnosis addDiagnosis(DiagnosisRequest diagnosisRequest) {
        Optional<Diagnosis> optionalDiagnosis = diagnosisRepository.findByName(diagnosisRequest.getName());
        if (optionalDiagnosis.isPresent()) {
            return optionalDiagnosis.get();
        }
        Diagnosis diagnosis = new Diagnosis(diagnosisRequest.getName());
        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public void deleteDiagnosis(long id) throws DiagnosisNotExistsException, DiagnosisIsUsedException {
        Diagnosis diagnosis = this.findDiagnosis(id);
        if (diagnosis.getPeoples().size() != 0) {
            throw new DiagnosisIsUsedException("Невозможно удалить диагноз, назначенный какому-либо пациенту");
        }
        diagnosisRepository.deleteById(id);
    }

}
