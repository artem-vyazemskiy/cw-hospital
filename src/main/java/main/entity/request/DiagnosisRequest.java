package main.entity.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DiagnosisRequest {

    private String name;

    @JsonCreator
    public DiagnosisRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
