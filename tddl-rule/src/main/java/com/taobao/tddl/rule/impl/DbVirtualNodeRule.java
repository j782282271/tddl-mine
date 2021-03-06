package com.taobao.tddl.rule.impl;

import com.taobao.tddl.rule.virtualnode.VirtualNodeMap;

import java.util.Map;

/**
 * @author <a href="junyu@taobao.com">junyu</a>
 * @version 1.0
 * @description
 * @date 2011-8-8 07:49:15
 * @since 1.6
 */
public class DbVirtualNodeRule extends VirtualNodeGroovyRule {

    public DbVirtualNodeRule(String expression, VirtualNodeMap vNodeMap) {
        super(expression, vNodeMap);
    }

    public DbVirtualNodeRule(String expression, VirtualNodeMap vNodeMap, String extraPackagesStr) {
        super(expression, vNodeMap, extraPackagesStr);
    }

    public String eval(Map<String, Object> columnValues, Object outerContext) {
        String key = (String) columnValues.get(EnumerativeRule.REAL_TABLE_NAME_KEY);
        return super.map(key);
    }
}
