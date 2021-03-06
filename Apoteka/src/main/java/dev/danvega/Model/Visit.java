package dev.danvega.Model;

import dev.danvega.Model.Enums.StatusCV;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name="visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = true)
    private LocalDate startDate;

    @Column(unique = false, nullable = true)
    private LocalTime startTime;

    @Column(unique = false, nullable = true)
    private int duration;

    @Column(unique = false, nullable = true)
    private double price;

    @Column(unique = false, nullable = true)
    private StatusCV status;

    @Column(unique = false, nullable = true)
    private String report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dermatologist_id")
    private Dermatologist dermatologist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apotecary_id")
    private Apotecary apotecary;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private MedicationReservation medicationReservation;

    public Visit(Long id, LocalDate startDate, int duration, double price, StatusCV status) {
        this.id = id;
        this.startDate = startDate;
        this.duration = duration;
        this.price = price;
        this.status = status;
    }

    public Visit() {
    }

    public Visit(Long id, LocalDate startDate, LocalTime startTime, int duration, double price, int status, String report, Dermatologist dermatologist, Patient patient, Apotecary apotecary, MedicationReservation medicationReservation) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.price = price;
        this.status = StatusCV.fromInteger(status);
        this.report = report;
        this.dermatologist = dermatologist;
        this.patient = patient;
        this.apotecary = apotecary;
        this.medicationReservation = medicationReservation;
    }

    public Visit(Long id, LocalDate startDate, LocalTime startTime, int duration, double price, int status, String report) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.price = price;
        this.status = StatusCV.fromInteger(status);
        this.report = report;

    }

    public Visit(Long id, LocalDate startDate, LocalTime startTime, int duration, double price, int status, String report, Dermatologist a, Patient b, Apotecary c) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.price = price;
        this.status = StatusCV.fromInteger(status);
        this.report = report;
        this.dermatologist = a;
        this.patient = b;
        this.apotecary = c;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public StatusCV getStatus() {
        return status;
    }

    public void setStatus(StatusCV status) {
        this.status = status;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Dermatologist getDermatologist() {
        return dermatologist;
    }

    public void setDermatologist(Dermatologist dermatologist) {
        this.dermatologist = dermatologist;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Apotecary getApotecary() {
        return apotecary;
    }

    public void setApotecary(Apotecary apotecary) {
        this.apotecary = apotecary;
    }

    public MedicationReservation getMedicationReservation() {
        return medicationReservation;
    }

    public void setMedicationReservation(MedicationReservation medicationReservation) {
        this.medicationReservation = medicationReservation;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}