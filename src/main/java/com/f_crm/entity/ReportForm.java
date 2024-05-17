package com.f_crm.entity;

import org.springframework.boot.autoconfigure.batch.BatchDataSource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
 public class ReportForm {
    private Integer selectedMonth;
    private Integer selectedYear;
}
