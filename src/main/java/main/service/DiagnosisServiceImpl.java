package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.exception.DiagnosisIsUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import main.entity.Diagnosis;
import main.entity.request.DiagnosisRequest;
import main.exception.DiagnosisNotExistsException;
import main.repository.DiagnosisRepository;
import main.repository.PersonRepository;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    PersonRepository personRepository;

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
    public Diagnosis findDiagnosis(String name) throws DiagnosisNotExistsException {
        Optional<Diagnosis> optionalDiagnosis = diagnosisRepository.findByName(name);
        return optionalDiagnosis.orElseThrow(() -> new DiagnosisNotExistsException("Диагноз не найден"));
    }

    @Override
    public Pair<Diagnosis, Boolean> addDiagnosis(DiagnosisRequest diagnosisRequest) {
        Optional<Diagnosis> optionalDiagnosis = diagnosisRepository.findByName(diagnosisRequest.getName());
        if (optionalDiagnosis.isPresent()) {
            return Pair.of(optionalDiagnosis.get(), false);
        }
        Diagnosis diagnosis = new Diagnosis(diagnosisRequest.getName());
        return Pair.of(diagnosisRepository.save(diagnosis), true);
    }

    @Override
    public boolean deleteDiagnosis(long id) throws DiagnosisIsUsedException {
        Diagnosis diagnosis = null;
        try {
            diagnosis = this.findDiagnosis(id);
        } catch (DiagnosisNotExistsException e) {
            return false;
        }
        if (diagnosis.getPeople().size() != 0) {
            throw new DiagnosisIsUsedException("Невозможно удалить диагноз, назначенный какому-либо пациенту");
        }
        diagnosisRepository.deleteById(id);
        return true;
    }

}
