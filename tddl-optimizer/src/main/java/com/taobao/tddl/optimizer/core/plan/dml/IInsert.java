package com.taobao.tddl.optimizer.core.plan.dml;

import com.taobao.tddl.optimizer.core.expression.ISelectable;
import com.taobao.tddl.optimizer.core.plan.IPut;

import java.util.List;

/**
 * @since 5.0.0
 */
public interface IInsert extends IPut<IInsert> {

    public List<Object> getDuplicateUpdateValues();

    void setDuplicateUpdateValues(List<Object> duplicateUpdateValues);

    public List<ISelectable> getDuplicateUpdateColumns();

    void setDuplicateUpdateColumns(List<ISelectable> duplicateUpdateColumns);

}
