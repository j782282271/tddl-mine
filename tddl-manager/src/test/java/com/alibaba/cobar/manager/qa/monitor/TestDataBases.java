package com.alibaba.cobar.manager.qa.monitor;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestDataBases extends TestCobarAdapter {

    @Test
    public void testDataBasesNum() {
        List<String> dataBasesList = cobarAdapter.listDataBases();
        Assert.assertTrue(dataBasesList.size() > 0);
    }
}
