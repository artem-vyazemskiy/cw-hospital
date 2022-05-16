package main.service;

import java.util.List;

import main.entity.Ward;
import main.entity.request.WardRequest;
import main.exception.WardAlreadyExistsException;
import main.exception.WardIncorrectMaxCountException;
import main.exception.WardIsUsedException;
import main.exception.WardNotExistsException;
import org.springframework.data.util.Pair;


public interface WardService {

    boolean exists(long id);
    List<Ward> listWards();
    List<Ward> listNotFullWards();
    List<Ward> listWardsWithDiagnosis(long diagnosisId);
    List<Ward> listWardsWithOneDiagnosis();
    List<Ward> listWardsWithDifferentDiagnoses();
    Ward findWard(long id) throws WardNotExistsException;
    Ward findWard(String name) throws WardNotExistsException;
    Pair<Ward, Boolean> addWard(WardRequest wardRequest) throws WardAlreadyExistsException;
    boolean updateWard(long id, WardRequest wardRequest)
            throws WardNotExistsException, WardAlreadyExistsException, WardIncorrectMaxCountException;
    boolean deleteWard(long id) throws WardIsUsedException;

}
