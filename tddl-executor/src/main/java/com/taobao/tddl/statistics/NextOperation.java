package com.taobao.tddl.statistics;

import com.taobao.tddl.executor.rowset.IRowSet;

import java.text.MessageFormat;

public class NextOperation extends AbstractSQLOperation {

    public final static MessageFormat message = new MessageFormat("Get a row: {0}");
    public IRowSet row;

    public NextOperation(IRowSet row) {
        super();
        this.row = row;
    }

    @Override
    public String getOperationString() {
        return message.format(new String[]{row.toString()});
    }

    @Override
    public String getOperationType() {
        return "Fetch";
    }

    @Override
    public String getSqlOrResult() {
        return null;
    }

}
