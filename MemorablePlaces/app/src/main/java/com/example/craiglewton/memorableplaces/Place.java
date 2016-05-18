package com.example.craiglewton.memorableplaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Craig.Lewton on 18/05/2016.
 */
public class Place {

    public Place(){

    }
    public Place(String label, LatLng latLng) {
        this.label = label;
        this.latLng = latLng;
    }
    public Place(LatLng latLng) {
        this.label = latLng.toString();
        this.latLng = latLng;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private String label;
    private LatLng latLng;

}
