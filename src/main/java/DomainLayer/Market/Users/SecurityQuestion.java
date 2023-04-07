package DomainLayer.Market.Users;

import java.util.UUID;

public class SecurityQuestion {
    private UUID clientCredentials;
    private String question;
    private String answer;

    public SecurityQuestion(UUID clientCredentials, String question, String answer){
        this.answer = answer;
        this.question = question;
        this.clientCredentials = clientCredentials;
    }

    public boolean validateAnswer(String answer){
        return this.answer.equals(answer);
    }
}
