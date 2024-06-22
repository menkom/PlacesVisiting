package info.mastera.imageservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.StringJoiner;

@Entity
public class StorageObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long placeId;

    @Column(nullable = false, length = 250)
    private String fileKey;

    public Long getId() {
        return id;
    }

    public StorageObject setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public StorageObject setPlaceId(Long placeId) {
        this.placeId = placeId;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public StorageObject setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StorageObject.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("placeId=" + placeId)
                .add("fileKey='" + fileKey + "'")
                .toString();
    }
}
