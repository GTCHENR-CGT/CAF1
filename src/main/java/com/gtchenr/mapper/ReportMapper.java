package com.gtchenr.mapper;

import com.gtchenr.pojo.Report;

import java.util.List;

public interface ReportMapper {

    List<Report> queryReports();

    Integer add(Report report);

    Report queryReportById(Integer id);

    Report queryReportInfoById(Integer id);

    List<Report> queryReportsInfo();

    Integer delete(Integer id);

    Integer update(Report report);

    List<Report> queryReportByUserId(Integer userId);
}
