package com.example.itemservice.Controllers;

import com.example.itemservice.Models.Customer;
import com.example.itemservice.Models.Item;
import com.example.itemservice.Repos.ItemRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Items", description = "Operations related to managing data concerning Items")
public class ItemController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer-service.url}")
    private String customerServiceUrl;
    private final ItemRepo itemRepo;

    public ItemController(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }
  //  @RequestMapping("/getById1/{id}")
    //public @ResponseBody Item getItem1(@PathVariable Long id){
      //  return itemRepo.findById(id).orElse(null);
    //}
    @GetMapping("/getById/{id}")
    @Operation(summary = "fetches an Item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch of Item successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "The request was malformed or had invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Item not found",
                    content = @Content)
    })
    public @ResponseBody Item getItem(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }
    @PostMapping("/add")
    public @ResponseBody Item addItem3(@Valid @RequestBody Item item){
        itemRepo.save(item);
        return item;
    }
    @GetMapping("/getAll")
    @Operation(summary = "Fetches all Items", description = "Fetches all Items in db and returns them as a List in JSON format")
    public @ResponseBody List<Item> getItems2() {
        return itemRepo.findAll();
    }

    @GetMapping("/getAllCustomers")
    public @ResponseBody Customer[] getCustomers() {
        String customerResourceUrl = customerServiceUrl + "/customers/getAll";
        return restTemplate.getForObject(customerResourceUrl, Customer[].class);
    }

    @GetMapping("/sell/{id}") //ingen trådning dock, ingen rollback
    @Operation(summary = "Sells one item.",
            description = "Checks if item with given ID is in stock and returns a String with success/failure message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Item sale successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "The request was malformed or had invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Item not found",
                    content = @Content)
    })
    public String sellItem(@PathVariable Long id) {
        Item item = itemRepo.findById(id).orElse(null);
        if(item!=null){
            if(item.getStock()>0){
                item.setStock(item.getStock()-1);
                itemRepo.save(item);
                return "Stock of item \"" + item.getName() + "\" is reduced to " + item.getStock();
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

