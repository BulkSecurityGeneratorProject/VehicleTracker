package com.zuehlke.camp.vehicletracker.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Location.
 */

@Document(collection = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("device_id")
    private String deviceId;

    @NotNull
    @Field("longitude")
    private Double longitude;

    @NotNull
    @Field("latitude")
    private Double latitude;

    @NotNull
    @Field("time")
    private ZonedDateTime time;

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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        if(location.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", deviceId='" + deviceId + "'" +
            ", longitude='" + longitude + "'" +
            ", latitude='" + latitude + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
