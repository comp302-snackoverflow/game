package tr.edu.ku.comp302.database;

import org.junit.Test;
import org.junit.Assert;

import tr.edu.ku.comp302.domain.handler.DatabaseHandler;

import java.sql.Connection;


public class TestDatabase {
    @Test
    public void TestConnection() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        Connection conn = dbHandler.getConnection();
        Assert.assertNotNull(conn);
    }
}
