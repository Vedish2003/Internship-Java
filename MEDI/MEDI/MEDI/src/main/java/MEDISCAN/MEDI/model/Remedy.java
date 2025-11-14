package MEDISCAN.MEDI.model;

import jakarta.persistence.*;

@Entity
@Table(name = "remedies")
public class Remedy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symptom;

    @Column(length = 1000)
    private String homeRemedy;

    @Column(length = 1000)
    private String instantSolution;

    public Remedy() {}

    public Remedy(String symptom, String homeRemedy, String instantSolution) {
        this.symptom = symptom;
        this.homeRemedy = homeRemedy;
        this.instantSolution = instantSolution;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public String getHomeRemedy() { return homeRemedy; }
    public void setHomeRemedy(String homeRemedy) { this.homeRemedy = homeRemedy; }

    public String getInstantSolution() { return instantSolution; }
    public void setInstantSolution(String instantSolution) { this.instantSolution = instantSolution; }

    @Override
    public String toString() {
        return "Remedy{" +
                "id=" + id +
                ", symptom='" + symptom + '\'' +
                ", homeRemedy='" + homeRemedy + '\'' +
                ", instantSolution='" + instantSolution + '\'' +
                '}';
    }
}
