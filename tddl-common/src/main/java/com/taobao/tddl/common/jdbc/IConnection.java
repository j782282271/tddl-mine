package com.taobao.tddl.common.jdbc;

import com.taobao.tddl.common.exception.TddlException;
import com.taobao.tddl.common.model.SqlMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IConnection extends Connection {

    public void kill() throws SQLException;

    public void cancelQuery() throws SQLException;

    public long getLastInsertId();

    public void setLastInsertId(long id);

    public List<Long> getGeneratedKeys();

    public void setGeneratedKeys(List<Long> ids);

    public ITransactionPolicy getTrxPolicy();

    public void setTrxPolicy(ITransactionPolicy trxPolicy) throws TddlException;

    public String getEncoding();

    public void setEncoding(String encoding);

    public String getSqlMode();

    public void setSqlMode(String sqlMode);

    /**
     * 传递该sql的元信息给底层
     *
     * @param sqlMetaData
     */
    public void setMetaData(SqlMetaData sqlMetaData);

    /**
     * 获取sql meta data
     */
    public SqlMetaData getSqlMetaData();
}
