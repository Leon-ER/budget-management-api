package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Service.ReportGenerationService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@Validated
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Retrieves a report comparing income vs. expenses for a given user over a specified time period.
     *
     * @param userId   The ID of the user whose report is requested (must be greater than 0).
     * @param startDate The start date for the report (ISO format: YYYY-MM-DD).
     * @param endDate   The end date for the report (ISO format: YYYY-MM-DD).
     * @param category  (Optional) The category of transactions to filter the report.
     * @param locale    The locale for internationalized messages.
     * @return  the transaction summary if successful or an error message if the user is not found, request is invalid, or an internal server error occurs.
     */
    @GetMapping("/income-vs-expenses/{userId}")
    public ResponseEntity<Object> getIncomeVsExpensesReport(
            @Min(1) @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {

        try {
            TransactionSummary report = reportGenerationService.calculateIncomeVsExpenses(userId, startDate, endDate, category);
            return ResponseEntity.ok(report);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("error.user.notfound", new Object[]{userId}, locale));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(messageSource.getMessage("errors.reports.badRequest", null, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.reports.serverGetting", null ,locale));
        }
    }
}


