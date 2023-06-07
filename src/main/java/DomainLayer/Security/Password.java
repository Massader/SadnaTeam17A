package DomainLayer.Security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "passwords")
public class Password {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String encryptedPassword;

    public Password(){

    }

    public Password(UUID id, String encryptedPassword) {
        this.id = id;
        this.encryptedPassword =encryptedPassword ;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}

