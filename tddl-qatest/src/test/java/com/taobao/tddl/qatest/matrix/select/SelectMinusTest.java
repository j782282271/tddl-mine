package com.taobao.tddl.qatest.matrix.select;

/**
 * Copyright(c) 2010 taobao. All rights reserved.
 * 通用产品测试
 */

import com.taobao.tddl.qatest.BaseMatrixTestCase;
import com.taobao.tddl.qatest.BaseTestCase;
import com.taobao.tddl.qatest.ExecuteTableName;
import com.taobao.tddl.qatest.util.EclipseParameterized;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 负值查询
 *
 * @author zhuoxue
 * @since 5.0.1
 */
@RunWith(EclipseParameterized.class)
public class SelectMinusTest extends BaseMatrixTestCase {

    public SelectMinusTest(String normaltblTableName) {
        BaseTestCase.normaltblTableName = normaltblTableName;
    }

    @Parameters(name = "{index}:table={0}")
    public static List<String[]> prepareData() {
        return Arrays.asList(ExecuteTableName.normaltblTable(dbType));
    }

    @Before
    public void MutilDataPrepare() throws Exception {
        normaltblPrepare(-10, 20);
    }

    /**
     * 列上负值
     *
     * @author zhuoxue
     * @since 5.0.1
     */
    @Test
    public void cloumMinusTest() throws Exception {
        String sql = String.format("select -id as a from %s where name=?", normaltblTableName);
        List<Object> param = new ArrayList<Object>();
        param.add(name);
        String[] columnParam = {"a"};
        selectContentSameAssert(sql, columnParam, param);

        sql = String.format("select pk-id as a from %s where name=?", normaltblTableName);
        selectContentSameAssert(sql, columnParam, param);

        sql = String.format("select pk-(-id) as a from %s where name=?", normaltblTableName);
        selectContentSameAssert(sql, columnParam, param);

        // sql = String.format("select pk---id as a from %s where name=?",
        // normaltblTableName);
        // selectContentSameAssert(sql, columnParam, param);

        // sql = String.format("select pk--(-id) as a from %s where name=?",
        // normaltblTableName);
        // selectContentSameAssert(sql, columnParam, param);
    }

    /**
     * 条件中负值
     *
     * @author zhuoxue
     * @since 5.0.1
     */
    @Test
    public void conditionMinusTest() throws Exception {
        String sql = String.format("select * from %s where id<pk-?", normaltblTableName);
        List<Object> param = new ArrayList<Object>();
        param.add(50);
        String[] columnParam = {"PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP", "GMT_DATETIME"};
        selectContentSameAssert(sql, columnParam, param);

        // sql = String.format("select * from %s where pk<id--?",
        // normaltblTableName);
        // selectContentSameAssert(sql, columnParam, param);
        //
        // sql = String.format("select * from %s where pk<id---?",
        // normaltblTableName);
        // selectContentSameAssert(sql, columnParam, param);
        //
        // sql = String.format("select * from %s where pk<id---(-?)",
        // normaltblTableName);
        // selectContentSameAssert(sql, columnParam, param);
    }
}
