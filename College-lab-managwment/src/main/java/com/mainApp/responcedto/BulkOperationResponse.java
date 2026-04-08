package com.mainApp.responcedto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class BulkOperationResponse {
    private Integer totalProcessed;
    private Integer successCount;
    private Integer failureCount;
    private List<String> errorMessages = new ArrayList<>();
    private List<String> successfulIds = new ArrayList<>();
}
