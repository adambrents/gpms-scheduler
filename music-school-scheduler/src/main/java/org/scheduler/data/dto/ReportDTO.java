package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.IComboBox;

public class ReportDTO implements IComboBox {

    private String reportName;
    private Reports reportEnum;

    /**
     * constructor for ReportDTO
     * @param reportName
     * @param reportId
     */
    public ReportDTO(String reportName, Reports reportId) {
        this.reportName = reportName;
        this.reportEnum = reportId;
    }

    public ReportDTO() {
        
    }

    /**
     * getter/setter for ReportDTO
     * @return
     */
    @Override
    public String getName() {
        return reportName;
    }

    public Reports getReportEnum() {
        return reportEnum;
    }

    public enum Reports{
        INSTRUMENT_LESSONS_RATIO,
        NUMBER_STUDENTS_INSTRUMENTS,
        GOLDCUP,
        NEW_STUDENTS
    }
}
