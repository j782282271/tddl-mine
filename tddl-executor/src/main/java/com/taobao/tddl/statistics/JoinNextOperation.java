package com.taobao.tddl.statistics;

import com.taobao.tddl.executor.rowset.IRowSet;

import java.text.MessageFormat;

public class JoinNextOperation extends AbstractSQLOperation {

    public final static MessageFormat message = new MessageFormat("Join a row: {0}");
    public IRowSet row;

    public JoinNextOperation(IRowSet row) {
        super();
        this.row = row;
    }

    @Override
    public String getOperationString() {
        return message.format(new String[]{row.toString()});
    }

    @Override
    public String getOperationType() {
        return "Join";
    }

    @Override
    public String getSqlOrResult() {
        return null;
    }

}
