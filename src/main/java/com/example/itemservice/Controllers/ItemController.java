package com.example.itemservice.Controllers;

import com.example.itemservice.Exception.ItemNotFoundException;
import com.example.itemservice.Models.Customer;
import com.example.itemservice.Models.Item;
import com.example.itemservice.Repos.ItemRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @GetMapping("/getById1/{id}")
    public @ResponseBody Item getItem1(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }
  /*  @GetMapping("/getById/{id}")
    public @ResponseBody Item getItem(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }

   @GetMapping(path = "/getById/{id}")
   public ResponseEntity<Item> getById(@PathVariable Long id) {
       Optional<Item> item = itemRepo.findById(id);
       if (item.isPresent()) {
           return ResponseEntity.ok(item.get());
       } else {
           throw new ItemNotFoundException("Item not found with ID: " + id);
       }
   }

   @GetMapping("/getById/{id}")
    public @ResponseBody Item getItem(@PathVariable Long id){
        if (itemRepo.findById(id).isPresent()) {
            return itemRepo.findById(id).get();
        } else {
            throw new ItemNotFoundException("Item not found with ID: " + id);
        }
    }
   */

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

    @GetMapping("/sell/{id}") //ingen trådning dock, ingen rollback
    public String sellItem(@PathVariable Long id) {
        Item item = itemRepo.findById(id).orElse(null);
        if(item!=null){
            if(item.getStock()>0){
                item.setStock(item.getStock()-1);
                itemRepo.save(item);
                return "Stock of item \"" + item.getName() + " price: " + item.getPrice() + "SEK" + "\" is reduced to " + item.getStock();
            } else {
                return "Could not sell item \"" + item.getName() + "\". Out of stock!";
            }
        } else {
            return "Item for id " + id + " not found.";
        }
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

