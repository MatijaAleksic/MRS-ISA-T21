package dev.danvega.Model;

import javax.persistence.*;

@Entity
@Table(name="administrators")
public class Administrator extends User {

    @ManyToOne
    @JoinColumn(name = "apotecary_id")
    private Apotecary apotecary;

    public Administrator(Long id, String firstName, String lastname, String username, String password, String email, String adress, String city, String country, String phone) {
        super(id, firstName, lastname, username, password, email, adress, city, country, phone);
    }

    public Administrator() {
    }

    public Administrator(Long id, String firstname, String lastname, String address, String city, String country, String phone) {
        super(id,firstname,lastname,address,city,country,phone);
    }

    public Administrator(Long id, String newPassword) {
        super(id,newPassword);
    }

    public Administrator(String firstname, String lastname, String username, String password, String email, String adress, String city, String country, String phone) {
        super(firstname, lastname,username,password,email,adress,city,country,phone);
    }

    public Administrator(String firstname, String lastname, String username, String password, String email, String adress, String city, String country, String phone, Long apotecary_id) {
        super(firstname, lastname,username,password,email,adress,city,country,phone);

    }


    public Apotecary getApotecary() {
        return apotecary;
    }

    public void setApotecary(Apotecary apotecary) {
        this.apotecary = apotecary;
    }
}
