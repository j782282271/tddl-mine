package com.taobao.tddl;

import com.taobao.tddl.group.jdbc.TGroupConnection;
import com.taobao.tddl.group.jdbc.TGroupDataSource;
import com.taobao.tddl.group.jdbc.TGroupPreparedStatement;
import org.junit.Test;

import java.sql.ResultSet;

/**
 * Created by jiangyang on 2019/1/4.
 */
public class MyTest {
    @Test
    public void test() throws Exception {
        TGroupDataSource source = new TGroupDataSource("imserver_group", "imserver");
        source.init();
        TGroupConnection tGroupConnection = source.getConnection();
        tGroupConnection.setAutoCommit(false);
        TGroupPreparedStatement preparedStatement = tGroupConnection.prepareStatement("select * from im_account where id = 1;");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.close();
        preparedStatement.close();
        tGroupConnection.close();
    }
}
