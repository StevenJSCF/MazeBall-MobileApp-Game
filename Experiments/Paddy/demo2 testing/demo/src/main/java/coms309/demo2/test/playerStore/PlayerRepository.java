package coms309.demo2.test.playerStore;

import coms309.demo2.test.playerStore.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUsername(String username);
}
