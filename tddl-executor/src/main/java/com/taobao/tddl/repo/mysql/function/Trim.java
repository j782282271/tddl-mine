package com.taobao.tddl.repo.mysql.function;

import com.taobao.tddl.common.jdbc.ParameterContext;
import com.taobao.tddl.optimizer.core.expression.IFunction;
import com.taobao.tddl.optimizer.core.plan.IDataNodeExecutor;
import com.taobao.tddl.repo.mysql.sqlconvertor.MysqlPlanVisitorImpl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Trim implements FunctionStringConstructor {

    @Override
    public String constructColumnNameForFunction(IDataNodeExecutor query, boolean bindVal,
                                                 AtomicInteger bindValSequence,
                                                 Map<Integer, ParameterContext> paramMap, IFunction func,
                                                 MysqlPlanVisitorImpl parentVisitor) {
        StringBuilder sb = new StringBuilder();

        sb.append("TRIM").append("(");

        if (func.getArgs().size() == 2) {
            sb.append(parentVisitor.getNewVisitor(func.getArgs().get(0)).getString());
        } else {
            sb.append(func.getArgs().get(2)).append(" ");
            sb.append(parentVisitor.getNewVisitor(func.getArgs().get(1)).getString());
            sb.append(" FROM ");
            sb.append(parentVisitor.getNewVisitor(func.getArgs().get(0)).getString());
        }

        sb.append(")");

        return sb.toString();

    }

    @Override
    public String[] getFunctionNames() {
        return new String[]{"TRIM"};
    }

}
