package UnitTests;
import DomainLayer.Market.UserController;
import ServiceLayer.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;

    @Before
    public void setUp() {
        userController = UserController.getInstance();
        userController.init();
    }
    @Test
    public void testRegisterSuccess() {
        String username = "testuser";
        String password = "password123";
        Response<Boolean> response = userController.register(username, password);

        assertEquals(true, response.getMessage());
    }

    @Test
    public void testRegisterFailure() {
        String username = "testuser";
        String password = "password123";
        Response<Boolean> response1 = userController.register(username, password);
        assertEquals(true, response1.getMessage());

        Response<Boolean> response2 = userController.register(username, "newpassword");
        assertEquals(false, response2.getMessage());
        assertEquals("This username is already in use.", response2.getMessage());
    }
}