package com.taobao.tddl.optimizer.core.plan.query;

public interface IShowWithTable extends IShow {

    public String getTableName();

    public void setTableName(String tableName);

    public String getActualTableName();

    public void setActualTableName(String actualTableName);
}
