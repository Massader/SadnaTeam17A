package DataAccessLayer;

import DomainLayer.Security.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, UUID> {

    // Custom methods for security question-related operations

}