package com.alibaba.asc.xuanwu.framework.common.tddl.util;

/**
 * ��TddlPartition.java��ʵ��������
 * P4P cobar�����ֿ����
 *
 * @author liangliang.zhangll 2013-1-22 ����04:34:54
 */
public class TddlPartition {

    // ��������:���ݶηֲ����壬����ȡģ����һ��Ҫ��2^n�� ��Ϊ����ʹ��x % 2^n == x & (2^n - 1)��ʽ�����Ż����ܡ�
    private static final int PARTITION_LENGTH = 1024;// 2^10

    // %ת��Ϊ&�����Ļ�����ֵ
    private static final long AND_VALUE = PARTITION_LENGTH - 1;

    /**
     * ��long��key���зֿ⣬���طֿ�id
     *
     * @param key          һ��Ϊ�ͻ�ID
     * @param partitionCnt �ֿ���� ����Ϊ2^nֵ
     * @return
     */
    public static int partitionLong(long key, int partitionCnt) {
        int[] segment = new int[PARTITION_LENGTH];
        if (partitionCnt <= 0 || partitionCnt > PARTITION_LENGTH
                || PARTITION_LENGTH % partitionCnt != 0) {
            throw new RuntimeException(
                    "error,check your partition length definition.");
        }

        int index = 0;
        int[] ai = new int[partitionCnt + 1];
        int length = PARTITION_LENGTH / partitionCnt;
        for (int j = 0; j < partitionCnt; j++) {
            ai[++index] = ai[index - 1] + length;
        }
        if (ai[ai.length - 1] != PARTITION_LENGTH) {
            throw new RuntimeException(
                    "error,check your partition length definition.");
        }
        // ����ӳ�����
        for (int i = 1; i < ai.length; i++) {
            for (int j = ai[i - 1]; j < ai[i]; j++) {
                segment[j] = (i - 1);
            }
        }
        return (segment[(int) (key & AND_VALUE)] + 1);
    }

    public static void main(String[] args) {

        System.out.println(TddlPartition.partitionLong(123456789L, 4));
    }
}
