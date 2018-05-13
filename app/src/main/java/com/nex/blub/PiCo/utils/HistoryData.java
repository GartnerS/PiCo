package com.nex.blub.PiCo.utils;

public class HistoryData {

    private final Integer timestamp;

    private final Double temp;

    private final Double hum;


    public HistoryData(Integer timestamp, Double temperature, Double humidity) {
        this.timestamp  = timestamp;
        this.temp       = temperature;
        this.hum        = humidity;
    }


    public Integer getTimestamp() {
        return this.timestamp;
    }


    public Double getHumidity() {
        return hum;
    }


    public Double getTemperature() {
        return temp;
    }
}
