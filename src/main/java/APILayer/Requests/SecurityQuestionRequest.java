package APILayer.Requests;

import java.util.UUID;

public class SecurityQuestionRequest extends Request{

    private String question;
    private String answer;

    public SecurityQuestionRequest(UUID clientCredentials, String question, String answer) {
        super(clientCredentials);
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
