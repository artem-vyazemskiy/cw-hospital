package main.service;

import java.util.*;

import main.exception.WardIsUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import main.entity.Ward;
import main.entity.Person;
import main.entity.request.WardRequest;
import main.exception.WardAlreadyExistsException;
import main.exception.WardNotExistsException;
import main.repository.WardRepository;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    private WardRepository wardRepository;

    @Override
    public boolean exists(long id) {
        return wardRepository.existsById(id);
    }

    @Override
    public List<Ward> listWards() {
        return (ArrayList<Ward>) wardRepository.findAll();
    }

    @Override
    public List<Ward> listNotFullWards() {
        List<Ward> notFullWards = new ArrayList<>();
        for (Ward ward : wardRepository.findAll()) {
            if (ward.getPeople().size() < ward.getMaxCount()) {
                notFullWards.add(ward);
            }
        }
        return notFullWards;
    }

    @Override
    public List<Ward> listWardsWithDiagnosis(long diagnosisId) {
        return wardRepository.findByDiagnosisId(diagnosisId);
    }

    @Override
    public List<Ward> listWardsWithOneDiagnosis() {
        List<Ward> wardsWithOneDiagnosis = new ArrayList<>();
        for (Ward ward : this.listWards()) {
            if (this.isWardWithOneDiagnosis(ward)) {
                wardsWithOneDiagnosis.add(ward);
            }
        }
        return wardsWithOneDiagnosis;
    }

    @Override
    public List<Ward> listWardsWithDifferentDiagnoses() {
        List<Ward> wardsWithDifferentDiagnosis = new ArrayList<>();
        for (Ward ward : this.listWards()) {
            if (ward.getPeople().size() == 0) {
                continue;
            }
            if (!this.isWardWithOneDiagnosis(ward)) {
                wardsWithDifferentDiagnosis.add(ward);
            }
        }
        return wardsWithDifferentDiagnosis;
    }

    @Override
    public Ward findWard(long id) throws WardNotExistsException {
        Optional<Ward> optionalWard = wardRepository.findById(id);
        return optionalWard.orElseThrow(() -> new WardNotExistsException("Палата не найдена"));
    }

    @Override
    public Ward findWard(String name) throws WardNotExistsException {
        Optional<Ward> optionalWard = wardRepository.findByName(name);
        return optionalWard.orElseThrow(() -> new WardNotExistsException("Палата не найдена"));
    }

    @Override
    public Pair<Ward, Boolean> addWard(WardRequest wardRequest) throws WardAlreadyExistsException {
        Optional<Ward> optionalWard = wardRepository.findByName(wardRequest.getName());
        if (optionalWard.isPresent()) {
            if (optionalWard.get().getMaxCount() == wardRequest.getMaxCount()) {
                return Pair.of(optionalWard.get(), false);
            }
            throw new WardAlreadyExistsException("Палата с таким названием уже существует и ее вместимость отличается");
        }
        Ward ward = new Ward(wardRequest.getName(), wardRequest.getMaxCount());
        return Pair.of(wardRepository.save(ward), true);
    }

    @Override
    public boolean updateWard(long id, WardRequest wardRequest) throws WardNotExistsException {
        Ward ward = this.findWard(id);
        boolean updated = false;
        if (!ward.getName().equals(wardRequest.getName())) {
            ward.setName(wardRequest.getName());
            wardRepository.updateName(ward.getId(), wardRequest.getName());
            updated = true;
        }
        if (ward.getMaxCount() != wardRequest.getMaxCount()) {
            ward.setMaxCount(wardRequest.getMaxCount());
            wardRepository.updateMaxCount(ward.getId(), wardRequest.getMaxCount());
            updated = true;
        }
        return updated;
    }

    @Override
    public boolean deleteWard(long id) throws WardIsUsedException {
        Ward ward = null;
        try {
            ward = this.findWard(id);
        } catch (WardNotExistsException e) {
            return false;
        }
        if (ward.getPeople().size() != 0) {
            throw new WardIsUsedException("Невозможно удалить палату, назначенную какому-либо пациенту");
        }
        wardRepository.deleteById(id);
        return true;
    }

    private boolean isWardWithOneDiagnosis(Ward ward) {
        List<Person> people = ward.getPeople();
        int peopleSize = people.size();
        if (peopleSize == 0) {
            return false;
        }
        long diagnosisId = people.get(0).getDiagnosis().getId();
        boolean oneDiagnosis = true;
        for (int i = 1; i < peopleSize; i++) {
            if (diagnosisId != people.get(i).getDiagnosis().getId()) {
                oneDiagnosis = false;
                break;
            }
        }
        return oneDiagnosis;
    }

}
