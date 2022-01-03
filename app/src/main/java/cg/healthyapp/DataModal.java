package cg.healthyapp;

public class DataModal {
    private String date;
    private String value;
    private String hour;

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DataModal(String date, String hour, String value) {
        this.date = date;
        this.hour = hour;
        this.value = value;
    }
}