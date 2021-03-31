package dev.danvega.Model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @SequenceGenerator(name="userSeq", sequenceName ="userSeqGen", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeq")
    private Long id;

    @Column(unique = false, nullable = true)
    private String firstName;

    @Column(unique = false, nullable = true)
    private String lastname;

    @Column(unique = false, nullable = true)
    private String username;

    @Column(unique = false, nullable = true)
    private String password;

    @Column(unique = false, nullable = true)
    private String email;

    @Column(unique = false, nullable = true)
    private String adress;

    @Column(unique = false, nullable = true)
    private String city;

    @Column(unique = false, nullable = true)
    private String country;

    @Column(unique = false, nullable = true)
    private String phone;

    public User(Long id, String firstName, String lastname, String username, String password, String email, String adress, String city, String country, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.adress = adress;
        this.city = city;
        this.country = country;
        this.phone = phone;
    }

    //IZBRISI KAD DOVRSIMO BACKEND OVO SAMO SLUZI DA SE NE CRVENI KOD ZA PRVI SPRINT STO SMO RADILI
    public User(String firstName, String lastname, String username, String password, String email) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
