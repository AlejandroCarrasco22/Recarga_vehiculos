package com.uva.dbcs.recharge.Model;

import com.uva.dbcs.recharge.Model.enums.Payment;
import com.uva.dbcs.recharge.Model.enums.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Recarga")
public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "userId")
    @Basic(optional = false)
    private Integer userId;

    @Column(name = "vehicleId")
    @Basic(optional = false)
    private Integer vehicleId;

    @Column(name = "chargerPointId")
    @Basic(optional = false)
    private Integer chargerpointId;

    @Column(name = "price")
    @Basic(optional = false)
    private double price; //precio de la recarga en kWh

    @Column(name = "kw")
    @Basic(optional = false)
    private double kw;

    @Column(name = "status")
    @Basic(optional = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "payment")
    @Basic(optional = false)
    @Enumerated(value = EnumType.STRING)
    private Payment payment;

    @Column(name = "datestart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;

    @Column(name = "dateend")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;

    public Recharge() {
    }

    public Recharge(Integer userId, Integer vehicleId, Integer chargepointId) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.chargerpointId = chargepointId;
        this.kw = 0;
        this.status = Status.NOTSTARTED;
        this.payment = Payment.NOTPROCESSED;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getChargerpointId() {
        return chargerpointId;
    }

    public void setChargerpointId(Integer chargepointId) {
        this.chargerpointId = chargepointId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getKw() {
        return kw;
    }

    public void setKw(double kw) {
        this.kw = kw;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
