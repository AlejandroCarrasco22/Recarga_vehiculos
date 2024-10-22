package com.uva.dbcs.users.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Entity
@Table(name = "Usuario")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Usuario {

    // Los atributos con "unique = true" tienen esa anotación
    // porque, como se nos indica en el enunciado de la práctica,
    // deben ser únicos.
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic (optional = false)
    @Column(name = "id", unique = true)
    private Integer id;

    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "name", unique = true)
    private String name;

    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "firstname")
    private String firstName;

    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "lastname")
    private String lastName;

    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "email", unique = true)
    private String email;

    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "password")
    private String password;

    // Se ha eliminado la anotación "optional = false" de @Basic, pues se especifica en el enunciado
    // que el usuario puede no haber añadido un método de pago.
    @Size(max = 16)
    @Column(name = "paymentcard")
    private String paymentCard;

    @Column(name = "enabled", columnDefinition = "TINYINT(1)")
    private Boolean enabled;

    @Column(name = "createdat")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updatedat")
    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private Date updatedAt;

    /**
    public Usuario(Integer id, String name, String firstName, String lastName,
                   String email, String password, String paymentCard, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.paymentCard = paymentCard;
        if(enabled == null){
            this.enabled = false;
        } else {
            this.enabled = enabled;
        }
    }
     */

    public Usuario(String name, String firstName, String lastName, String email, String password, String paymentCard) throws NoSuchAlgorithmException {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        String passwordHash = Base64.getEncoder().encodeToString(hash);
        this.password = passwordHash;
        this.paymentCard = paymentCard;
        this.enabled = false;
    }

    public Usuario() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(String paymentCard) {
        this.paymentCard = paymentCard;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
