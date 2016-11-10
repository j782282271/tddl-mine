package com.taobao.tddl.repo.oceanbase.handler;

import com.taobao.tddl.common.jdbc.ParameterContext;
import com.taobao.tddl.repo.mysql.handler.ExplainMyHandler;

import java.math.BigDecimal;
import java.util.Map;

public class ExplainObHandler extends ExplainMyHandler {

    /**
     * 为ob搞的，ob不支持bigdecimal，统一转double
     *
     * @param params
     */
    @Override
    public void convertBigDecimal(Map<Integer, ParameterContext> params) {
        for (ParameterContext paramContext : params.values()) {
            Object value = paramContext.getValue();
            if (value instanceof BigDecimal) {
                value = ((BigDecimal) value).doubleValue();
                paramContext.setValue(value);
            }
        }
    }

}
