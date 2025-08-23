package database.dao;

import model.Utente;

import java.sql.SQLException;
import java.util.List;

public interface DAO_Generico<Type, Key> {

    boolean save(Utente utente, String password) throws SQLException;

    boolean delete(Key key) throws SQLException;

    Type findByKey(Key key) throws SQLException;

    List<Type> findAll() throws SQLException;
}
