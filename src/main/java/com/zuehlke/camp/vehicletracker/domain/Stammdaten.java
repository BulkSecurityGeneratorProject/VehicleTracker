package com.zuehlke.camp.vehicletracker.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Stammdaten.
 */

@Document(collection = "stammdaten")
public class Stammdaten implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("device_id")
    private String deviceId;

    @NotNull
    @Field("driver")
    private String driver;

    @NotNull
    @Field("vehicle")
    private String vehicle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stammdaten stammdaten = (Stammdaten) o;
        if(stammdaten.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stammdaten.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stammdaten{" +
            "id=" + id +
            ", deviceId='" + deviceId + "'" +
            ", driver='" + driver + "'" +
            ", vehicle='" + vehicle + "'" +
            '}';
    }
}
