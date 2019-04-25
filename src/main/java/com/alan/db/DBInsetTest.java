package com.alan.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/25 16:28
 * @Description: TODO
 */
public class DBInsetTest extends DBMsgBase {

    private String testValue;

    public DBInsetTest(Integer DBMsgID,String testValue) {
        super(DBMsgID);
        this.testValue = testValue;
    }

    @Override
    public PreparedStatement callStoreProcedure(Connection connection) throws SQLException {

        CallableStatement callableStatement = connection.prepareCall("call table01_save(?)");
        callableStatement.setString(1,testValue);
        return callableStatement;

    }
}
