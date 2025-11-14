package MEDISCAN.MEDI.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mediconnect_bridge")
public class MediconnectBridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bridge_id") // ✅ correct primary key column
    private int bridgeId;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "available_beds")
    private int availableBeds;

    @Column(name = "available_icu")
    private int availableIcu;

    @Column(name = "available_ambulance")
    private int availableAmbulance;

    @Column(name = "available_ventilators")
    private int availableVentilators;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "specialties")
    private String specialties;

    // ✅ Getters and Setters
    public int getBridgeId() {
        return bridgeId;
    }

    public void setBridgeId(int bridgeId) {
        this.bridgeId = bridgeId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(int availableBeds) {
        this.availableBeds = availableBeds;
    }

    public int getAvailableIcu() {
        return availableIcu;
    }

    public void setAvailableIcu(int availableIcu) {
        this.availableIcu = availableIcu;
    }

    public int getAvailableAmbulance() {
        return availableAmbulance;
    }

    public void setAvailableAmbulance(int availableAmbulance) {
        this.availableAmbulance = availableAmbulance;
    }

    public int getAvailableVentilators() {
        return availableVentilators;
    }

    public void setAvailableVentilators(int availableVentilators) {
        this.availableVentilators = availableVentilators;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }
}
