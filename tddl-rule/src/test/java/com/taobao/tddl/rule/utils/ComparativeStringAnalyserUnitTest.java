package com.taobao.tddl.rule.utils;

import com.taobao.tddl.common.model.sqljep.Comparative;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="junyu@taobao.com">junyu</a>
 * @version 1.0
 * @date 2011-9-16 11:00:47
 * @since 1.6
 */
public class ComparativeStringAnalyserUnitTest {

    @Test
    public void testDecodeComparative_Date() {
        String conditionStr = "gmt_create = 2011-11-11 11:11:11.0 : date";
        Comparative comp = ComparativeStringAnalyser.decodeComparative(conditionStr, null);
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) comp.getValue());
        Assert.assertEquals(2011, cal.get(Calendar.YEAR));
        Assert.assertEquals(10, cal.get(Calendar.MONTH));
        Assert.assertEquals(11, cal.get(Calendar.DATE));
        Assert.assertEquals(11, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(11, cal.get(Calendar.MINUTE));
        Assert.assertEquals(11, cal.get(Calendar.SECOND));

        String conditionStr2 = "gmt_create = 2011-11-11 11:11:11.0 : d";
        Comparative comp2 = ComparativeStringAnalyser.decodeComparative(conditionStr2, null);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime((Date) comp2.getValue());
        Assert.assertEquals(2011, cal2.get(Calendar.YEAR));
        Assert.assertEquals(10, cal2.get(Calendar.MONTH));
        Assert.assertEquals(11, cal2.get(Calendar.DATE));
        Assert.assertEquals(11, cal2.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(11, cal2.get(Calendar.MINUTE));
        Assert.assertEquals(11, cal2.get(Calendar.SECOND));

        String conditionStr3 = "gmt_create=2011-11-11:d";
        Comparative comp3 = ComparativeStringAnalyser.decodeComparative(conditionStr3, null);
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime((Date) comp3.getValue());
        Assert.assertEquals(2011, cal3.get(Calendar.YEAR));
        Assert.assertEquals(10, cal3.get(Calendar.MONTH));
        Assert.assertEquals(11, cal3.get(Calendar.DATE));
        Assert.assertEquals(0, cal3.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal3.get(Calendar.MINUTE));
        Assert.assertEquals(0, cal3.get(Calendar.SECOND));
    }

    @Test
    public void testDecodeComparative_string() {
        String conditionStr = "message in(hi:hi,hi:hi2,hi:hi3):s";
        Comparative comp = ComparativeStringAnalyser.decodeComparative(conditionStr, null);
        Assert.assertEquals("(=HI:HI) OR (=HI:HI2) OR (=HI:HI3)", comp.toString());

        String conditionStr2 = "message in (hi:hi,hi:hi2,hi:hi3):string";
        Comparative comp2 = ComparativeStringAnalyser.decodeComparative(conditionStr2, null);
        Assert.assertEquals("(=HI:HI) OR (=HI:HI2) OR (=HI:HI3)", comp2.toString());
    }

    @Test
    public void testDecodeComparative_int() {
        String conditionStr = "message_id in(1,2,3):int";
        Comparative comp = ComparativeStringAnalyser.decodeComparative(conditionStr, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp.toString());

        String conditionStr2 = "message_id in(1,2,3):i";
        Comparative comp2 = ComparativeStringAnalyser.decodeComparative(conditionStr2, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp2.toString());
    }

    @Test
    public void testDecodeComparative_long() {
        String conditionStr = "message_id in(1,2,3):long";
        Comparative comp = ComparativeStringAnalyser.decodeComparative(conditionStr, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp.toString());

        String conditionStr2 = "message_id in(1,2,3):l";
        Comparative comp2 = ComparativeStringAnalyser.decodeComparative(conditionStr2, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp2.toString());
    }

    @Test
    public void testDecodeComparative_null() {
        String conditionStr = "message_id in(1,2,3)";
        Comparative comp = ComparativeStringAnalyser.decodeComparative(conditionStr, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp.toString());

        String conditionStr2 = "message_id in(1,2,3)";
        Comparative comp2 = ComparativeStringAnalyser.decodeComparative(conditionStr2, null);
        Assert.assertEquals("(=1) OR (=2) OR (=3)", comp2.toString());
    }
}
