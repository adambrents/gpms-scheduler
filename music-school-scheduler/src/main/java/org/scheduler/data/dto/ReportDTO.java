package org.scheduler.data.dto;

public class ReportDTO {

    private String reportName;
    private int reportId;

    /**
     * constructor for ReportDTO
     * @param reportName
     * @param reportId
     */
    public ReportDTO(String reportName, int reportId) {
        this.reportName = reportName;
        this.reportId = reportId;
    }

    public ReportDTO() {
        
    }

    /**
     * getter/setter for ReportDTO
     * @return
     */
    public String getReportName() {
        return reportName;
    }

    //
//    /**
//     * getter/setter for ReportDTO
//     * @return
// 
////     */
////    public int getReportId() {
////        return reportId;
////    }
//// 
//
//    /**
//     * getter/setter for ReportDTO
// 
//     * @param reportId
//     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
