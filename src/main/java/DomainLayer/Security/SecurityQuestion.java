package DomainLayer.Security;

import java.util.UUID;

public class SecurityQuestion {
    UUID clientCredentials;
    String question;
    String answer;


    public SecurityQuestion(UUID clientCredentials, String question, String answer) {
        this.clientCredentials = clientCredentials;
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }
    public Boolean validateAnswer1(String answer ) {
        if (this.answer.equals(answer)){return  true;}
        return false;
    }


}
