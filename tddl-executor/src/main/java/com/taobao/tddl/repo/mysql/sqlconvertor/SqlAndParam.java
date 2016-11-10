package com.taobao.tddl.repo.mysql.sqlconvertor;

import com.taobao.tddl.common.jdbc.ParameterContext;
import com.taobao.tddl.common.utils.GeneralUtil;

import java.util.Map;

public class SqlAndParam {

    public String sql;
    public Map<Integer, ParameterContext> param;
    public Map<Integer, Integer> newParamIndexToOld;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sql: ").append(sql);

        if (GeneralUtil.isNotEmpty(param)) {
            sb.append("\n").append("param: ").append(param).append("\n");
        }
        return sb.toString();
    }
}
