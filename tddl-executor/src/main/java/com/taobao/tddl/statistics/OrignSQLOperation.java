package com.taobao.tddl.statistics;

import java.text.MessageFormat;

public class OrignSQLOperation extends AbstractSQLOperation {

    public final static MessageFormat message = new MessageFormat("Execute sql on {0}, sql is: {1}, params is: {2}");
    String sql;

    public OrignSQLOperation(String sql) {
        super();
        this.sql = sql;
    }

    @Override
    public String getOperationString() {

        return message.format(new String[]{sql});

    }

    @Override
    public String getSqlOrResult() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String getOperationType() {
        return "Orign SQL";
    }

}
