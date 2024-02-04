package com.example.User_Login.playerStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("item/all")
    List<Item> GetAllItems() {return itemRepository.findAll(); }

    @GetMapping("getItemById/{id}")
    Item fetchDetailsById(@PathVariable Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
    }

    @GetMapping("price/{id}")
    int getItemPrice(@PathVariable Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for id: " + id))
                .getPrice();
    }

    @PostMapping("item/post/{n}/{p}")
    Item PostItemByPath(@PathVariable String n,@PathVariable String p){
        Item newItem=new Item();
        newItem.setName(n);
        newItem.setPrice(Integer.parseInt(p));
        itemRepository.save(newItem);
        return newItem;
    }

    @GetMapping("/item/{id}")
    Item getItem(@PathVariable Long id){
        return itemRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

    //Updating the item name
    @PutMapping("/updateNameById/{id}")
    Item updateName(@RequestBody Item updatedItem, @PathVariable Long id) {
        Item oldName = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
        oldName.setName(updatedItem.getName());
        itemRepository.save(oldName);
        return oldName;
    }

    // Updating the item price
    @PutMapping("/updatePriceById/{id}")
    Item updatePassword(@RequestBody Item updatedPrice, @PathVariable Long id) {
        Item oldPrice= itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
        oldPrice.setPrice(updatedPrice.getPrice());
        itemRepository.save(oldPrice);
        return oldPrice;
    }

    @DeleteMapping("/deleteItemById/{id}")
    String deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return "Deleted Item with id:" + id;
    }

}
