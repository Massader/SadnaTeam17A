package UnitTests;

import DomainLayer.Security.SecurityQuestion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SecurityQuestionTest {

    @Test
    public void testValidateAnswerWithCorrectAnswer() {
        // Create a security question
        UUID clientCredentials = UUID.randomUUID();
        String question = "What is your favorite color?";
        String answer = "Blue";
        SecurityQuestion securityQuestion = new SecurityQuestion(clientCredentials, question, answer);

        // Validate the answer with the correct answer
        String userAnswer = "Blue";
        boolean isValid = securityQuestion.validateAnswer(userAnswer);

        // Ensure the answer is valid
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testValidateAnswerWithIncorrectAnswer() {
        // Create a security question
        UUID clientCredentials = UUID.randomUUID();
        String question = "What is your favorite color?";
        String answer = "Blue";
        SecurityQuestion securityQuestion = new SecurityQuestion(clientCredentials, question, answer);

        // Validate the answer with an incorrect answer
        String userAnswer = "Green";
        boolean isValid = securityQuestion.validateAnswer(userAnswer);

        // Ensure the answer is not valid
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testValidateAnswerWithEmptyAnswer() {
        // Create a security question
        UUID clientCredentials = UUID.randomUUID();
        String question = "What is your favorite color?";
        String answer = "Blue";
        SecurityQuestion securityQuestion = new SecurityQuestion(clientCredentials, question, answer);

        // Validate the answer with an empty answer
        String userAnswer = "";
        boolean isValid = securityQuestion.validateAnswer(userAnswer);

        // Ensure the answer is not valid
        Assertions.assertFalse(isValid);
    }
}
