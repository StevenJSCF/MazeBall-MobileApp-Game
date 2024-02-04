package coms309.demo2.test.playerStore;

import org.springframework.data.jpa.repository.JpaRepository;
import coms309.demo2.test.playerStore.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
}
