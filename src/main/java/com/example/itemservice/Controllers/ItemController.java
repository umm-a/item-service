package com.example.itemservice.Controllers;

import com.example.itemservice.Models.Customer;
import com.example.itemservice.Models.Item;
import com.example.itemservice.Repos.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
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
/*
    @RequestMapping("/getAll")
    public List<Item> getItems(){
        return itemRepo.findAll();
    }*/
    @RequestMapping("/getById1/{id}")
    public @ResponseBody Item getItem1(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }
    @GetMapping("/getById/{id}")
    public @ResponseBody Item getItem(@PathVariable Long id){
        return itemRepo.findById(id).orElse(null);
    }

    //curl http://localhost:8080/items/add -H "Content-Type:application/json" -d "{\"name\":\"Lola-shirt\", \"price\":1745, \"stock\":5}" -v
   /* @PostMapping("/add")
    public String addItem(@RequestBody Item item){
        itemRepo.save(item);
        return "Saved " + item.getName();
    }*/
    //Används ej mellan microservices
    @PostMapping("/addWithParams/{name}/{price}/{stock}")
    public @ResponseBody Item addItem2(@PathVariable String name, @PathVariable int price, @PathVariable int stock){
        Item item = new Item(name, price, stock);
        itemRepo.save(item);
        return item;
    }
    @PostMapping("/postItem")
    public @ResponseBody Item addItem3(@RequestBody Item item){
        itemRepo.save(item);
        return item;
    }

    //Används till postman etc, GET
    @GetMapping("/getAll")
    public @ResponseBody List<Item> getItems2() {
        return itemRepo.findAll();
    }

    @GetMapping("/getAllCustomers")
    public @ResponseBody Customer[] getCustomers() {
        String customerResourceUrl = customerServiceUrl + "/customers/getAll";
        return restTemplate.getForObject(customerResourceUrl, Customer[].class);
    }
}
