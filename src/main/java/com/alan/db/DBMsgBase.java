package com.alan.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/25 15:14
 * @Description: TODO
 */
public class DBMsgBase {

    private Integer DBMsgID;

    DBMsgBase(Integer DBMsgID) {
        this.DBMsgID = DBMsgID;
    }

    public PreparedStatement callStoreProcedure(Connection connection) throws SQLException {
        return null;
    }

}
