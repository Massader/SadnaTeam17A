package DomainLayer.Security;

import java.util.UUID;

public class SecurityQuestion {
    private UUID clientCredentials;
    private String question;
    private String answer;

    public SecurityQuestion(UUID clientCredentials, String question, String answer) {
        this.clientCredentials = clientCredentials;
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }
    public Boolean validateAnswer(String answer ) {
        if (this.answer.equals(answer)){return  true;}
        return false;
    }

}
