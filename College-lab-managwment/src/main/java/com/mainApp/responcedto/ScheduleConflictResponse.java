package com.mainApp.responcedto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleConflictResponse {
    private boolean hasConflict;
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        this.hasConflict = true;
        this.messages.add(message);
    }
}
