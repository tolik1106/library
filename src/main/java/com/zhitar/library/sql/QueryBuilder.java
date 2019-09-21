package com.zhitar.library.sql;

import org.apache.log4j.Logger;

public class QueryBuilder {

    private static final Logger LOG = Logger.getLogger(QueryBuilder.class.getName());

    private static final String COMMA = ", ";
    private static final String SPACE = " ";
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String SELECT_ALL = SELECT + "*" + FROM;
    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = ") VALUES (";
    private static final String OPEN_BRACER = " (";
    private static final String CLOSE_BRACER = ") ";
    private static final String Q_MARK = "?";
    private static final String UPDATE = "UPDATE ";
    private static final String SET = " SET ";
    private static final String ASSIGN = " = ?";
    private static final String WHERE = " WHERE ";
    private static final String DELETE = "DELETE FROM ";
    private static final String JOIN = " JOIN ";
    private static final String ON = " ON ";
    private static final String EQUALS = " = ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String REGEXP = " REGEXP ";
    private static final String IN = " IN ";
    private static final String LIMIT = " LIMIT ";
    private StringBuilder builder = new StringBuilder();

    public String build() {
        LOG.debug("Building query: " + builder.toString());
        return builder.toString();
    }

    public QueryBuilder select(String... params) {
        if (params.length == 0) {
            builder.append(SELECT_ALL);
        } else {
            builder.append(SELECT);
            int i = 0;
            for (; i < params.length - 1; i++) {
                builder.append(params[i])
                        .append(COMMA);
            }
            builder.append(params[i])
                    .append(FROM);
        }
        return this;
    }

    public QueryBuilder insert(String tableName, String... params) {
        builder.append(INSERT)
                .append(tableName)
                .append(OPEN_BRACER);
        StringBuilder closingResult = new StringBuilder(VALUES);
        int i = 0;
        for (; i < params.length - 1 ; i++) {
            builder.append(params[i])
                    .append(COMMA);
            closingResult.append(Q_MARK)
                    .append(COMMA);
        }
        builder.append(params[i])
                .append(closingResult)
                .append(Q_MARK)
                .append(CLOSE_BRACER);
        return this;
    }

    public QueryBuilder update(String tableName, String... params) {
        builder.append(UPDATE)
                .append(tableName)
                .append(SET);
        int i = 0;
        for (; i < params.length - 1; i++) {
            builder.append(params[i])
                    .append(ASSIGN)
                    .append(COMMA);
        }
        builder.append(params[i])
                .append(ASSIGN);
        return this;
    }

    public QueryBuilder delete(String tableName) {
        builder.append(DELETE)
                .append(tableName);
        return this;
    }

    public QueryBuilder join(String type, String tableWithAlias, String param1, String param2) {
        builder.append(SPACE)
                .append(type)
                .append(JOIN)
                .append(tableWithAlias)
                .append(ON)
                .append(param1)
                .append(EQUALS)
                .append(param2);
        return this;
    }

    public QueryBuilder whereAssign(String param) {
        builder.append(WHERE)
                .append(param)
                .append(ASSIGN);
        return this;
    }

    public QueryBuilder whereRegexp(String param) {
        builder.append(WHERE)
                .append(param)
                .append(REGEXP)
                .append(Q_MARK);
        return this;
    }

    public QueryBuilder whereIn(String param, int size) {
        builder.append(WHERE)
                .append(param)
                .append(IN)
                .append(OPEN_BRACER);
        int i = 0;
        for (; i < size - 1; i++) {
            builder.append(Q_MARK)
                    .append(COMMA);
        }
        builder.append(Q_MARK)
                .append(CLOSE_BRACER);
        return this;
    }

    public QueryBuilder order(String... params) {
        builder.append(ORDER_BY);
        int i = 0;
        for (; i < params.length - 1; i++) {
            builder.append(params[i])
                    .append(COMMA);
        }
        builder.append(params[i]);
        return this;
    }

    public QueryBuilder table(String tableName) {
        builder.append(SPACE).append(tableName).append(SPACE);
        return this;
    }

    public QueryBuilder limit(int limit) {
        builder.append(LIMIT).append(limit).append(SPACE);
        return this;
    }

    public QueryBuilder offset(int offset) {
        builder.append(" OFFSET ").append(offset).append(SPACE);
        return this;
    }
}
