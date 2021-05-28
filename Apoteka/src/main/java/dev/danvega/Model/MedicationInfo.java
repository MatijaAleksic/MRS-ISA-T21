package dev.danvega.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="medicationInfo")
public class MedicationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = true)
    private double price;

    @Column(unique = false, nullable = true)
    private LocalDate priceDurationEndDate;

    @Column(unique = false, nullable = true)
    private LocalTime priceDurationEndTime;

    @Column(unique = false, nullable = true)
    private int inStorage;

    @OneToMany(mappedBy = "medicationInfo", fetch = FetchType.LAZY)
    private List<MedicationReservation> medicationReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apotecary_id", nullable = true)
    private Apotecary apotecary;



    public MedicationInfo() {
    }

    public MedicationInfo(Long id, double price, LocalDate priceDurationEndDate, LocalTime priceDurationEndTime, int inStorage, List<MedicationReservation> medicationReservation, Medication medication, Apotecary apotecary) {
        this.id = id;
        this.price = price;
        this.priceDurationEndDate = priceDurationEndDate;
        this.priceDurationEndTime = priceDurationEndTime;
        this.inStorage = inStorage;
        this.medicationReservation = medicationReservation;
        this.medication = medication;
        this.apotecary = apotecary;
    }

    public MedicationInfo(double price, LocalDate priceDurationEndDate, LocalTime priceDurationEndTime, int inStorage, Apotecary apotecary, Medication medication) {
        this.price = price;
        this.priceDurationEndDate = priceDurationEndDate;
        this.priceDurationEndTime = priceDurationEndTime;
        this.inStorage = inStorage;
        this.medication = medication;
        this.apotecary = apotecary;
    }

    public MedicationInfo(double price, LocalDate priceDurationEndDate, LocalTime priceDurationEndTime, int inStorage) {
        this.price = price;
        this.priceDurationEndDate = priceDurationEndDate;
        this.priceDurationEndTime = priceDurationEndTime;
        this.inStorage = inStorage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStorage() {
        return inStorage;
    }

    public void setInStorage(int inStorage) {
        this.inStorage = inStorage;
    }

    public List<MedicationReservation> getMedicationReservation() {
        return medicationReservation;
    }

    public void setMedicationReservation(List<MedicationReservation> medicationReservation) {
        this.medicationReservation = medicationReservation;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Apotecary getApotecary() {
        return apotecary;
    }

    public void setApotecary(Apotecary apotecary) {
        this.apotecary = apotecary;
    }

    public LocalDate getPriceDurationEndDate() {
        return priceDurationEndDate;
    }

    public void setPriceDurationEndDate(LocalDate priceDurationEndDate) {
        this.priceDurationEndDate = priceDurationEndDate;
    }

    public LocalTime getPriceDurationEndTime() {
        return priceDurationEndTime;
    }

    public void setPriceDurationEndTime(LocalTime priceDurationEndTime) {
        this.priceDurationEndTime = priceDurationEndTime;
    }
}
