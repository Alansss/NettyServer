package com.alan.db;

import com.alan.config.GlobalConfigManager;
import com.alan.log.NetLog;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class DatabaseManager extends Thread {

    private static DatabaseManager instance = null;

    private static DruidDataSource dataSource = null;

    private final Queue<DBMsgBase> queueDBQuery = new LinkedList<>();
    private final Queue<DBMsgBase> queueDBResult = new LinkedList<>();

    private DatabaseManager() {
        setName("DatabaseManager");
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void init() {

        GlobalConfigManager globalConfigManager = GlobalConfigManager.getInstance();
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(globalConfigManager.getDriverClassName());
        dataSource.setUrl(globalConfigManager.getDbUrl());
        dataSource.setUsername(globalConfigManager.getDbUsername());
        dataSource.setPassword(globalConfigManager.getDbPassword());
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(30000);

        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                logicQueryDatabase();
                Thread.sleep(20);
            } catch (Exception e) {
                NetLog.Log(e.toString());
            }

        }
    }

    private void logicQueryDatabase() {
        if (queueDBQuery.size() > 0) {
            Queue<DBMsgBase> tmp = new LinkedList<>();
            synchronized (queueDBQuery) {
                while (queueDBQuery.size() > 0) {
                    tmp.add(queueDBQuery.poll());
                }
            }

            if (tmp.size() > 0) {
                Queue<DBMsgBase> ret = new LinkedList<>();
                while (tmp.size() > 0) {
                    DBMsgBase dbQuery = tmp.poll();
                    DBMsgBase dbResult = realQueryDatabase(dbQuery);
                    if (dbResult != null) {
                        ret.add(dbResult);
                    }
                }

                synchronized (queueDBResult) {
                    while (ret.size() > 0) {
                        queueDBResult.add(ret.poll());
                    }
                }
            }
        }
    }

    private DBMsgBase realQueryDatabase(DBMsgBase query) {
        DBMsgBase ret = null;
        PreparedStatement ps = null;

        try {
            ResultSet rs = null;
            DruidPooledConnection connection = dataSource.getConnection();
            ps = query.callStoreProcedure(connection);
            rs = ps.executeQuery();

            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public void sengMsgToDatabase(DBMsgBase query) {
        synchronized (queueDBQuery) {
            queueDBQuery.add(query);
        }
    }

}
