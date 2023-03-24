package com.example.marketplace.controlers;

import com.example.marketplace.models.Item;
import com.example.marketplace.models.ItemResp;
import com.example.marketplace.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RESTController {
    private final ItemService itemService;

    public RESTController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/item")
    private Iterable<Item> list() {
        return itemService.getAllItems();
    }

    @GetMapping("/item/{id}")
    private Optional<Item> getById(@PathVariable("id") Integer id) {
        return itemService.getById(id);
    }

    @DeleteMapping("/item/{id}")
    private void delete(@PathVariable("id") Integer id) {
        itemService.remove(id);
    }

    @PatchMapping("/item/{id}")
    private void update(@PathVariable("id") Integer id) {
        itemService.getById(id).ifPresent(item -> {
            item.setSale(!item.getSale());
            itemService.save(item);
        });

    }
    @PostMapping("/item")
    private Item addProduct(@RequestBody ItemResp response) {
        return itemService.add(response);
    }
}
