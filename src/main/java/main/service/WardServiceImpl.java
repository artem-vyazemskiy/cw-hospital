package main.service;

import java.util.*;

import javassist.NotFoundException;
import main.entity.Diagnosis;
import main.exception.DiagnosisIsUsedException;
import main.exception.WardIsUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entity.Ward;
import main.entity.People;
import main.entity.request.WardRequest;
import main.exception.WardAlreadyExistsException;
import main.exception.WardNotExistsException;
import main.repository.PeopleRepository;
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
            if (ward.getPeoples().size() < ward.getMaxCount()) {
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
            if (ward.getPeoples().size() == 0) {
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
    public Ward addWard(WardRequest wardRequest) throws WardAlreadyExistsException {
        Optional<Ward> optionalWard = wardRepository.findByName(wardRequest.getName());
        if (optionalWard.isPresent()) {
            if (optionalWard.get().getMaxCount() == wardRequest.getMaxCount()) {
                return optionalWard.get();
            }
            throw new WardAlreadyExistsException("Палата с таким названием уже существует и ее вместимость отличается");
        }
        Ward ward = new Ward(wardRequest.getName(), wardRequest.getMaxCount());
        return wardRepository.save(ward);
    }

    @Override
    public void updateWard(Ward ward, WardRequest wardRequest) {
        if (!ward.getName().equals(wardRequest.getName())) {
            ward.setName(wardRequest.getName());
            wardRepository.updateName(ward.getId(), wardRequest.getName());
        }
        if (ward.getMaxCount() != wardRequest.getMaxCount()) {
            ward.setMaxCount(wardRequest.getMaxCount());
            wardRepository.updateMaxCount(ward.getId(), wardRequest.getMaxCount());
        }
    }

    @Override
    public void deleteWard(long id) throws WardNotExistsException, WardIsUsedException {
        Ward ward = this.findWard(id);
        if (ward.getPeoples().size() != 0) {
            throw new WardIsUsedException("Невозможно удалить палату, назначенную какому-либо пациенту");
        }
        wardRepository.deleteById(id);
    }

    private boolean isWardWithOneDiagnosis(Ward ward) {
        List<People> peoples = ward.getPeoples();
        int peoplesSize = peoples.size();
        if (peoplesSize == 0) {
            return false;
        }
        long diagnosisId = peoples.get(0).getDiagnosis().getId();
        boolean oneDiagnosis = true;
        for (int i = 1; i < peoplesSize; i++) {
            if (diagnosisId != peoples.get(i).getDiagnosis().getId()) {
                oneDiagnosis = false;
                break;
            }
        }
        return oneDiagnosis;
    }

}
