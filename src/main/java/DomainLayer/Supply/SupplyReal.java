package DomainLayer.Supply;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import DomainLayer.Market.Users.ShoppingCart;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Dictionary;
import java.util.Hashtable;


public class SupplyReal implements SupplyBridge {
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
    public Integer supply(String name, String address, String city, String country, int zip){    // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Generate the POST request content
        MultiValueMap<String, String> postContent = generateSupplyPostContent(name, address, city, country, zip);

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
            return -1; // Supply transaction failed
        }
    }


    @Override
    public Integer cancel_supply(int transaction_id) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Generate the POST request content
        MultiValueMap<String, String> postContent = generateCancelSupplyPostContent(String.valueOf(transaction_id));

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

    public static MultiValueMap<String, String> generateSupplyPostContent(String name, String address, String city, String country, int zip) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();

        postContent.add("action_type", "supply");
        postContent.add("name", name);
        postContent.add("address", address);
        postContent.add("city", city);
        postContent.add("country", country);
        postContent.add("zip", String.valueOf(zip));

        return postContent;
    }

    public static MultiValueMap<String, String> generateCancelSupplyPostContent(String transactionId) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();

        postContent.add("action_type", "cancel_supply");
        postContent.add("transaction_id", transactionId);

        return postContent;
    }

//    public static void main(String[] args) {
//        SupplyReal payment = new SupplyReal();
//        Integer cancellationResult = payment.cancel_supply(20123);
//        System.out.println("Cancellation Result: " + cancellationResult);
//    }
}

