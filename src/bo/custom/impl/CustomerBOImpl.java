package bo.custom.impl;

import Entity.Customer;
import bo.custom.CustomerBO;
import dao.CrudUtil;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import db.DbConnection;
import dto.CustomerDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        ArrayList<Customer> all = customerDAO.getAll();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(customer.getCustomerId(), customer.getCustomerTitle(), customer.getCustomerName(),
                    customer.getCustomerAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode()));
        }
        return allCustomers;
    }


    @Override
    public boolean addCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(dto.getCustomerId(), dto.getCustomerTitle(), dto.getCustomerName(),
        dto.getCustomerAddress(),dto.getCity(),dto.getProvince(),dto.getPostalCode()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getCustomerId(), dto.getCustomerTitle(), dto.getCustomerName(),
                dto.getCustomerAddress(),dto.getCity(),dto.getProvince(),dto.getPostalCode()));
    }


    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.ifCustomerExist(id);
    }


    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewID();
    }

    @Override
    public Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer customer= customerDAO.getCustomer(id);
        return customer;
    }

}
