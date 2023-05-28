package com.example.itemservice.Controllers;

import com.example.itemservice.Models.Customer;
import com.example.itemservice.Models.Item;
import com.example.itemservice.Repos.ItemRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping ("/items")
public class ItemController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer-service.url}")
    private String customerServiceUrl;
    private final ItemRepo itemRepo;

    public ItemController(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }
    @RequestMapping("/getById1/{id}")
    public @ResponseBody Item getItem1(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }
    @GetMapping("/getById/{id}")
    public @ResponseBody Item getItem(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }
    @PostMapping("/addWithParams/{name}/{price}/{stock}")
    public @ResponseBody Item addItem2(@Valid @PathVariable String name, @Valid @PathVariable int price,@Valid @PathVariable int stock){
        Item item = new Item(name, price, stock);
        itemRepo.save(item);
        return item;
    }
    @PostMapping("/add")
    public @ResponseBody Item addItem3(@Valid @RequestBody Item item){
        itemRepo.save(item);
        return item;
    }
    @GetMapping("/getAll")
    public @ResponseBody List<Item> getItems2() {
        return itemRepo.findAll();
    }

    @GetMapping("/getAllCustomers")
    public @ResponseBody Customer[] getCustomers() {
        String customerResourceUrl = customerServiceUrl + "/customers/getAll";
        return restTemplate.getForObject(customerResourceUrl, Customer[].class);
    }

    //istället för att få ett långt stacktrace vill vi fånga upp felen och skriva ut felmeddelanden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

