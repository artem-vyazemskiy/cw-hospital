package main.entity.request;

public class DiagnosisRequest {

    private String name;

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
