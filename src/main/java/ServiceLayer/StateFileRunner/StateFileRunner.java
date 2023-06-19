package ServiceLayer.StateFileRunner;

import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@Component
public class StateFileRunner implements CommandLineRunner {
    
    private final ObjectMapper objectMapper;
    private final Service service;
    private final Map<String, Object> functionResults;
    private final BiConsumer biConsumer = (o, o2) -> { };
    
    public StateFileRunner(ObjectMapper objectMapper, Service service) {
        this.objectMapper = objectMapper;
        this.service = service;
        functionResults = new HashMap<>();
    }
    
    @Override
    public void run(String... args) throws Exception {
        File jsonFile = new File("src/main/resources/state.json"); // Path to your JSON file
        
        // Read JSON file and map it to a list of Function objects
        List<Function> functions = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Function.class));
        
        // Map to store function results
        functionResults.put("$cc", service.createClient().getValue());
        // Call functions and store the results
        for (Function function : functions) {
            Object result = callFunction(function.getName(), function.getArguments());
            if (function.getName().equals("login"))
                functionResults.put("$cc", ((ServiceUser)result).getId());
            else if (function.getName().equals("logout") || function.getName().equals("createClient"))
                functionResults.put("$cc", result);
            else if (function.getRetVal().equals("$ignore")){
                continue;
            }
            functionResults.put(function.getRetVal(), result);
        }
        
        // Print the function results
        System.out.println(functionResults);
    }
    
    // Send an HTTP request to the server with the function name and arguments
    private Object callFunction(String functionName, List<Object> arguments) {
        Response response = switch (functionName) {
            case "register" -> service.register((String) arguments.get(0), (String) arguments.get(1));
            case "login" -> service.login((UUID) functionResults.get("$cc"), (String) arguments.get(0), (String) arguments.get(1), biConsumer);
            case "createStore" -> service.createStore((UUID) functionResults.get("$cc"), (String) arguments.get(0), (String) arguments.get(1));
            case "appointStoreOwner" -> service.appointStoreOwner((UUID) functionResults.get("$cc"),
                    ((ServiceUser) functionResults.get((String)arguments.get(0))).getId(),
                    ((ServiceStore) functionResults.get((String)arguments.get(1))).getStoreId());
            case "appointStoreManager" -> service.appointStoreManager((UUID) functionResults.get("$cc"),
                    ((ServiceUser) functionResults.get((String)arguments.get(0))).getId(),
                    ((ServiceStore) functionResults.get((String)arguments.get(1))).getStoreId());
            case "logout" -> service.logout((UUID) functionResults.get("$cc"));
            case "addItem" -> service.addItemToStore((UUID) functionResults.get("$cc"), (String) arguments.get(0), (double) arguments.get(1),
                    (UUID) arguments.get(2), (int) arguments.get(3),(String) arguments.get(4));
            case "setManagerPermissions" -> service.setManagerPermissions((UUID) functionResults.get("$cc"),
                    ((ServiceUser) functionResults.get((String)arguments.get(0))).getId(),
                    ((ServiceStore) functionResults.get((String)arguments.get(1))).getStoreId(),
                    (List<Integer>) arguments.get(3));
            case "getUserByUsername" -> service.getUserByUsername((String) arguments.get(0));
            default -> null;
        };
        assert response != null;
        return handleResponse(response);
    }
    
    private <T> T handleResponse(Response<T> response) {
        if (response.isError())
            throw new RuntimeException("Error on state file reading - " + response.getMessage());
        else return response.getValue();
    }
}