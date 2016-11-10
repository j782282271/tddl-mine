package com.taobao.tddl.executor.common;

import com.taobao.tddl.common.jdbc.IConnection;
import com.taobao.tddl.common.jdbc.IDataSource;

import java.sql.SQLException;
import java.util.Set;

public interface IConnectionHolder {

    public IConnection getConnection(String groupName, IDataSource ds) throws SQLException;

    public void closeAllConnections();

    public void tryClose(IConnection conn, String groupName) throws SQLException;

    public Set<IConnection> getAllConnection();

    public void kill();

    public void cancel();

    public void restart();
}
