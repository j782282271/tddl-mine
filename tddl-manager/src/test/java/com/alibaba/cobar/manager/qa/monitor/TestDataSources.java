package com.alibaba.cobar.manager.qa.monitor;

import com.alibaba.cobar.manager.dataobject.cobarnode.DataNodesStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestDataSources extends TestCobarAdapter {

    @Test
    public void testDataSourcesCommand() {
        List<DataNodesStatus> dsStatusList = cobarAdapter.listDataNodes();
        Assert.assertTrue(dsStatusList.size() > 0);
    }
}
