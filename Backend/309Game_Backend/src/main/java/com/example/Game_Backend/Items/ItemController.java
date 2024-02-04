package com.example.Game_Backend.Items;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Tag(name = "Item API",description = "HTTP request mappings for items")
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @Operation(summary = "returns a list of all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Failure")
    })
    @GetMapping("item/all")
    public List<Item> GetAllItems() {return itemRepository.findAll(); }

    @Operation(summary = "gets item by {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Item not found for id: + id")
    })
    @GetMapping("getItemById/{id}")
    public Item fetchDetailsById(@PathVariable @Parameter(description = "item id", example = "1") Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
    }

    @Operation(summary = "gets the price of the item by {id}")
    @ApiResponses(value = {
            @ApiResponse(description = "success"),
            @ApiResponse(description = "Item not found for id: + id")
    })
    @GetMapping("price/{id}")
    public int getItemPrice(@PathVariable @Parameter(description = "item id", example = "1") Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for id: " + id))
                .getPrice();
    }

    @Operation(summary = "posts an item to the table",description = "posts an item {n} of price {p} to the table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Failure")
    })
    @PostMapping("item/post/{n}/{p}")
    public Item PostItemByPath(@PathVariable @Parameter(description = "item name", example = "+10s") String n,
                        @PathVariable @Parameter(description = "item price", example = "10") String p){
        Item newItem=new Item();
        newItem.setName(n);
        newItem.setPrice(Integer.parseInt(p));
        itemRepository.save(newItem);
        return newItem;
    }

    @Operation(summary = "gets an item by {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Item not found for id: + id")
    })
    @GetMapping("/item/{id}")
    public Item getItem(@PathVariable @Parameter(description = "item id", example = "1") Long id){
        return itemRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

    @Operation(summary = "updates the name of item {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Item not found for id: + id")
    })
    @PutMapping("/updateNameById/{id}/{name}")
    public Item updateName(@PathVariable @Parameter(description = "item id", example = "1") Long id,
                           @PathVariable @Parameter(description = "new name", example = "New Item Name") String name) {
        Item oldName = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
        oldName.setName(name);
        itemRepository.save(oldName);
        return oldName;
    }

    @Operation(summary = "updates {item} by {price}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Failure")
    })
    @PutMapping("/updatePriceById/{id}/{price}")
    public Item updatePrice(@PathVariable @Parameter(description = "item id", example = "1") Long id,
                            @PathVariable @Parameter(description = "new price", example = "15") int price) {
        Item oldPrice= itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found for id: " + id));
        oldPrice.setPrice(price);
        itemRepository.save(oldPrice);
        return oldPrice;
    }

    @Operation(summary = "deletes an item by {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Item not found for id: + id")
    })
    @DeleteMapping("/deleteItemById/{id}")
    public String deleteItem(@PathVariable @Parameter(description = "item id", example = "1") Long id) {
        itemRepository.deleteById(id);
        return "Deleted Item with id:" + id;
    }

}
