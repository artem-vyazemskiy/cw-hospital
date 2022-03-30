package main.entity.request;

public class WardRequest {

    private String name;
    private int maxCount;

    public WardRequest(String name, int maxCount) {
        this.name = name;
        this.maxCount = maxCount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
