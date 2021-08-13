package com.payconiq.stock.api.controller;

import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.api.model.StockResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Saeid Noroozi
 * @version 1.0
 * @apiNote StockEntity api allows users to view, create and update stocks.
 * @since 1.0
 */
@RestController
@RequestMapping("/api/stocks")
@Validated
public interface StockController {

    /**
     * Method used to retrieve all the stocks in the system based on pagination parameters
     * pagination support with from and size parameters.
     *
     * @param pageable that clear all requirements about pagination
     * @return Page<StockResponse> that support meta data (total, size, ...) with list of StockResponse
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Method used to retrieve all the stocks in the system based on pagination parameters. " +
            "Pagination support with url parameters")
    Page<StockResponse> getAllStocks(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable);

    @RequestMapping(value = "/{stock-id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    StockResponse getSpecificStock(@PathVariable("stock-id") Long stockId);


    @RequestMapping(value = "/{stock-id}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    StockResponse updateSpecificStock(@PathVariable("stock-id") Long stockId,
                                      @Valid @RequestBody StockRequest stockInput);


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    StockResponse creatNewStock(@Valid @RequestBody StockRequest stockRequest);

    @RequestMapping(value = "/{stock-id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSpecificStock(@PathVariable("stock-id") Long stockId);
}
