package com.zhitar.library.connection;

import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleMysqlConnector {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static final Logger LOG = Logger.getLogger(SingleMysqlConnector.class);

    static {
        DB_URL = PropertiesUtil.getValue("mysql.url");
        LOG.debug("DB_URL: " + DB_URL);
        DB_USER = PropertiesUtil.getValue("mysql.username");
        LOG.debug("DB_USER: " + DB_USER);
        DB_PASSWORD = PropertiesUtil.getValue("mysql.password");
        LOG.debug("DB_PASSWORD: " + DB_PASSWORD);

    }

    public static Connection getConnection() {
        LOG.info("getConnection invoke");
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            LOG.error("Couldn't get connection");
            throw new RuntimeException("Couldn't get connection", e);
        }
    }
}
