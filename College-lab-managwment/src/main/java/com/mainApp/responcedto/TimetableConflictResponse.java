package com.mainApp.responcedto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimetableConflictResponse {
    private boolean hasConflicts;
    private List<String> conflicts = new ArrayList<>();

    public void addConflict(String conflict) {
        this.hasConflicts = true;
        this.conflicts.add(conflict);
    }
}
