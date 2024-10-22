package com.uva.dbcs.vehicles.Model;

import com.uva.dbcs.vehicles.Model.Enums.PlugType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Vehiculo")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true)
    private Integer id;

    @Basic(optional = false)
    @Column(unique = true, name = "carregistration") // Matricula del vehiculo
    @Size(max = 7, min = 7) // Nº de caracteres de una matrícula en España
    private String carRegistration;

    @Column(name = "userId")
    @Basic(optional = true)
    private Integer userId;
    
    // Hace referencia a la marca del vehículo
    @Size(max = 50)
    @Column(name = "brand")
    @Basic(optional = false)
    private String brand;

    @Size(max = 50)
    @Column(name = "model")
    @Basic(optional = false)
    private String model;

    @Column(name = "capacity")
    @Basic(optional = false)
    private double capacity;

    @Column(name = "plugtype")
    @Basic(optional = false)
    @Enumerated(value = EnumType.STRING)
    private PlugType plugType;  // Enumeración para el tipo de conector


    public Vehiculo() {}

    public Vehiculo(String carRegistration, Integer userId, String brand, String model, double capacity, PlugType plugType) {
        this.carRegistration = carRegistration;
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.capacity = capacity;
        this.plugType = plugType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarRegistration() {
        return carRegistration;
    }

    public void setCarRegistration(String carRegistration) {
        this.carRegistration = carRegistration;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public PlugType getPlugType() {
        return plugType;
    }

    public void setPlugType(PlugType plugType) {
        this.plugType = plugType;
    }


}
