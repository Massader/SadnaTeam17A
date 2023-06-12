package DomainLayer.Payment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PaymentReal implements PaymentBridge{
    String url = "https://php-server-try.000webhostapp.com/";
    @Override
    public void setReal() {}

    @Override
    public String handshake() {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set the request body (action_type = handshake)
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "handshake");

        // Create the HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postContent, headers);

        // Send the POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Get the response body
        String response = responseEntity.getBody();
        return response;
    }




    @Override
    public Integer pay(String cardNumber, String month, String year, String holder, String cvv, String id) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Generate the POST request content
        MultiValueMap<String, String> postContent = generatePayPostContent(cardNumber, month, year, holder, cvv, id);

        // Create the HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postContent, headers);

        // Send the POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Get the response body
        String response = responseEntity.getBody();

        // Parse the response to an integer value
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Transaction failed
        }
    }



    @Override
    public Integer cancel_Pay(int transactionId) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Generate the POST request content
        MultiValueMap<String, String> postContent = generateCancelPayPostContent(transactionId);

        // Create the HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postContent, headers);

        // Send the POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Get the response body
        String response = responseEntity.getBody();

        // Parse the response to an integer value
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Cancellation failed
        }
    }







    //generatePostContent

    public static MultiValueMap<String, String> generatePayPostContent(String cardNumber, String month, String year, String holder, String cvv, String id) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();

        postContent.add("action_type", "pay");
        postContent.add("card_number", cardNumber);
        postContent.add("month", month);
        postContent.add("year", year);
        postContent.add("holder", holder);
        postContent.add("ccv", cvv);
        postContent.add("id", id);

        return postContent;
    }


    public static MultiValueMap<String, String> generateCancelPayPostContent(int transactionId) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();

        postContent.add("action_type", "cancel_pay");
        postContent.add("transaction_id", String.valueOf(transactionId));

        return postContent;
    }







}
