package main.service;

import java.util.List;

import main.entity.Ward;
import main.entity.request.WardRequest;
import main.exception.WardAlreadyExistsException;
import main.exception.WardIsUsedException;
import main.exception.WardNotExistsException;

public interface WardService {

    boolean exists(long id);
    List<Ward> listWards();
    List<Ward> listNotFullWards();
    List<Ward> listWardsWithDiagnosis(long diagnosisId);
    List<Ward> listWardsWithOneDiagnosis();
    List<Ward> listWardsWithDifferentDiagnoses();
    Ward findWard(long id) throws WardNotExistsException;
    Ward findWard(String name) throws WardNotExistsException;
    Ward addWard(WardRequest wardRequest) throws WardAlreadyExistsException;
    void updateWard(Ward ward, WardRequest wardRequest);
    void deleteWard(long id) throws WardNotExistsException, WardIsUsedException;

}
