package com.example.itemservice;

import com.example.itemservice.Models.Item;
import com.example.itemservice.Repos.ItemRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public CommandLineRunner run(ItemRepo itemRepo) throws Exception {
        return (String[] args) -> {
            Item item1 = new Item("Hubba Bubba Eatable Necklace", 129, 1);
            Item item2 = new Item("A Teeny Tiny Hat For Mice Especially", 20, 2);
            Item item3 = new Item("The Best Shoes There Ever Was", 89599, 0);
            Item item4 = new Item("Never-Again-Cya-Later-SUNGLASSES", 399, 1);
            itemRepo.save(item1);
            itemRepo.save(item2);
            itemRepo.save(item3);
            itemRepo.save(item4);
        };
    }
}
