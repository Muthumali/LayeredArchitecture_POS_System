package dao.custom;

import Entity.Item;
import dao.CrudDAO;
import dao.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ItemDAO extends CrudDAO<Item,String> {
    boolean ifItemExist(String code) throws SQLException, ClassNotFoundException;

    String generateNewID() throws SQLException, ClassNotFoundException;

    public Item getItem(String id) throws SQLException, ClassNotFoundException ;
}
