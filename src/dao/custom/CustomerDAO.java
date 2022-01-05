package dao.custom;

import Entity.Customer;
import dao.CrudDAO;
import dao.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer, String> {
    boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException;
    String generateNewID() throws SQLException, ClassNotFoundException;

    public Customer getCustomer(String id) throws SQLException, ClassNotFoundException ;

}
