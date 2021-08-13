package com.payconiq.stock.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalErrorResponse {
    private HttpStatus status;
    private String message;
    private String path;
    private List<String> details;
}
