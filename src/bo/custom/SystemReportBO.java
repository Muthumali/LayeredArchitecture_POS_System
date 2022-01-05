package bo.custom;

import Entity.SystemReport;
import bo.SuperBO;
import db.DbConnection;
import dto.SystemReportDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface SystemReportBO extends SuperBO {
    public String getMost(String iCode) throws SQLException, ClassNotFoundException ;

    public String getLeast(String iCode) throws SQLException, ClassNotFoundException ;
    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException ;

    public ArrayList<String> getYears() throws SQLException, ClassNotFoundException;

    public boolean isYearExists(String string , ArrayList<String> year);
    public ArrayList<String> getMonth() throws SQLException, ClassNotFoundException;

    public ArrayList<String> getDates() throws SQLException, ClassNotFoundException ;

    public ArrayList<SystemReportDTO> getYearlyDetails(String year) throws SQLException, ClassNotFoundException ;
    public ArrayList<SystemReportDTO> getMonthlyDetails(String month) throws SQLException, ClassNotFoundException ;

    public ArrayList<SystemReportDTO> getDailyDetails(String Day) throws SQLException, ClassNotFoundException ;

    public ArrayList<SystemReportDTO> getCustomerIncome(String id) throws SQLException, ClassNotFoundException ;
}
