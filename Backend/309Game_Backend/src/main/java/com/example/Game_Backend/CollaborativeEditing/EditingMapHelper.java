package com.example.Game_Backend.CollaborativeEditing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EditingMapHelper {

    private final EditingMapRepository editingMapRepo;

    @Autowired
    public EditingMapHelper(EditingMapRepository editingMapRepo) {
        this.editingMapRepo = editingMapRepo;
    }

    public String tableName() {
        String tname;

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        tname = "tempTable" + uuidString;

        return tname;
    }

    public void addChanges(Long sessionNum, String change) {
        EditingMap m = editingMapRepo.findById(sessionNum).orElse(null);

        if (m == null) {
            return;
        }

        String s = m.getChanges();

        String newS = s + " " + change;

        m.setChanges(newS);

        editingMapRepo.save(m);


    }

    public void deleteLastChange(Long sessionNum) {
        EditingMap m = editingMapRepo.findById(sessionNum).orElse(null);

        if (m == null) {
            return;
        }

        String changes = m.getChanges();

        // Use a regular expression to match the last group of digits
        String regex = "\\s\\d+$";

        // Replace the matched group with an empty string
        String newS = changes.replaceAll(regex, "");

        m.setChanges(newS);

        editingMapRepo.save(m);

    }
}
