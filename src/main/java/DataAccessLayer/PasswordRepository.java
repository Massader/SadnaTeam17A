package DataAccessLayer;

import DomainLayer.Security.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordRepository extends JpaRepository<Password, UUID> {
    // Custom methods for security question-related operations
}