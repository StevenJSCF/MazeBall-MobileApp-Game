package coms309.demo2.test.playerStore;

import coms309.demo2.test.playerStore.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import coms309.demo2.test.playerStore.Item;
import java.util.Optional;
@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepo;

    public Item getItemDetailsById(Long id){
        Optional<Item> itemOptional=itemRepo.findById(id);

        if (itemOptional.isPresent()) {
            return itemOptional.get(); // Extract the item from Optional
        } else {
            // Handle the case when the item is not found, e.g., by throwing an exception or returning null.
            throw new RuntimeException("Item not found for id: " + id);
        }
    }
}
