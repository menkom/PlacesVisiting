package info.mastera.userserviceapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Long ownerId;
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_place_trip"))
    private Place place;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> companions;

    public Long getId() {
        return id;
    }

    public Trip setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Trip setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Trip setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public Trip setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public Place getPlace() {
        return place;
    }

    public Trip setPlace(Place place) {
        this.place = place;
        return this;
    }

    public List<String> getCompanions() {
        return companions;
    }

    public Trip setCompanions(List<String> companions) {
        this.companions = companions;
        return this;
    }
}
