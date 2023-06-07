package DomainLayer.Security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "security_questions")
public class SecurityQuestion {

    @Id
    private UUID clientCredentials;
    @Column
    private String question;
    @Column
    private String answer;

    public SecurityQuestion(UUID clientCredentials, String question, String answer) {
        this.clientCredentials = clientCredentials;
        this.question = question;
        this.answer = answer;
    }
    public SecurityQuestion(){}
    public String getQuestion() {
        return question;
    }
    public Boolean validateAnswer(String answer ) {
        if (this.answer.equals(answer)){return  true;}
        return false;
    }

}
