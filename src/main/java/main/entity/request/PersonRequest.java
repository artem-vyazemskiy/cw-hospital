package main.entity.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import main.entity.component.FullName;

public class PersonRequest {

    private FullName fullName;
    private long wardId;
    private long diagnosisId;

    @JsonCreator
    public PersonRequest(FullName fullName, long wardId, long diagnosisId) {
        this.fullName = fullName;
        this.wardId = wardId;
        this.diagnosisId = diagnosisId;
    }

    public FullName getFullName() {
        return this.fullName;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public long getWardId() {
        return this.wardId;
    }

    public void setWardId(long wardId) {
        this.wardId = wardId;
    }

    public long getDiagnosisId() {
        return this.diagnosisId;
    }

    public void setDiagnosisId(long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

}
