package com.uva.dbcs.chargerpoints.Model;

import com.uva.dbcs.chargerpoints.Model.Enums.PlugType;
import com.uva.dbcs.chargerpoints.Model.Enums.PowerType;
import com.uva.dbcs.chargerpoints.Model.Enums.StatusType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PuntoRecarga")
public class PuntoRecarga {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic (optional = false)
    @Column(name = "id", unique = true)
    private Integer id;
     
    @Basic(optional = false)
    @Size(max = 60)
    @Column(name = "address")
    private String address;
    
    @Basic(optional = false)
    @Column(name = "latitude")
    private double latitude;
    
    @Basic(optional = false)
    @Column(name = "longitude")
    private double longitude;
    
    @Basic(optional = false)
    @Column(name = "plugtype")
    @Enumerated(value = EnumType.STRING)
    private PlugType plugType;  // Enumeración para el tipo de conector
    
    @Basic(optional = false)
    @Column(name = "power")
    @Enumerated(value = EnumType.STRING)
    private PowerType power;    // Enumeración para el tipo de cargador
    
    @Basic(optional = false)
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusType status;  // Enumeración para el estado del punto de carga*/
    
    public PuntoRecarga(){}
    
    public PuntoRecarga(Integer id, String address, double latitude, double longitude, PlugType plugType, PowerType power, StatusType status) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.plugType = plugType;
        this.power = power;
        this.status = status;
    }

    public PuntoRecarga(String address, double latitude, double longitude, PlugType plugType, PowerType power, StatusType status) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.plugType = plugType;
        this.power = power;
        this.status = status;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
     
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
   
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public PlugType getPlugType() {
        return plugType;
    }
     
    public void setPlugType(PlugType plugType) {
        this.plugType = plugType;
    }
    
    public PowerType getPower() {
        return power;
    }

    public void setPower(PowerType power) {
        this.power = power;
    }
    
    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
    
}
