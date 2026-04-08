package com.mainApp.requestdto;

import lombok.Data;
import java.util.Map;

@Data
public class TeacherReassignmentRequest {
    /**
     * Map of LabSession ID to the ID of the new Teacher assigned to it.
     * key: sessionId
     * value: targetTeacherId
     */
    private Map<String, String> sessionTeacherMap;
}
