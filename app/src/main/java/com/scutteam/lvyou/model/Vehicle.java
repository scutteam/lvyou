package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/8/20.
 */
@Table(name = "Vehicle")
public class Vehicle extends Model {
    
    @Column(name = "vehicle_id")
    public Long vehicle_id;

    @Column(name = "vehicle_name")
    public String vehicle_name;

    public static List<Vehicle> insertWithArray(JSONArray dataArray) {
        try {
            List<Vehicle>vehicleList = new ArrayList<Vehicle>();

            for(int i = 0; i < dataArray.length(); i++) {
                Vehicle vehicle = insertOrReplace(dataArray.getJSONObject(i));
                vehicleList.add(vehicle);
            }

            return vehicleList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vehicle insertOrReplace(JSONObject dataObject) {
        try {
            Long vehicle_id = 0L;
            if(dataObject.has("id")) {
                vehicle_id = dataObject.getLong("id");
            }
            Vehicle vehicle = new Vehicle();
            vehicle.vehicle_id = vehicle_id;

            if(dataObject.has("vehicleName")) {
                vehicle.vehicle_name = dataObject.optString("vehicleName");
            }

            return vehicle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
