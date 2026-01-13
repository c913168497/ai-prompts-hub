// 新建 CreateFinalPromptRequest.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateFinalPromptRequest {
    private List<Long> promptIds;
}