package info.mastera.userserviceapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table( indexes = {
        @Index(name = "uk_username_provider", columnList = "username, provider", unique = true),
})
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, length = 250)
    private String username;
    @Column(name = "password", length = 250)
    private String password;
    @Column(name="provider", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Account setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public Provider getProvider() {
        return provider;
    }

    public Account setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }
}
