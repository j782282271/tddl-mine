package com.taobao.tddl.common.jdbc;

import javax.sql.DataSource;
import java.sql.SQLException;

public interface IDataSource extends DataSource {

    @Override
    IConnection getConnection() throws SQLException;
}
