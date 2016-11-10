package com.taobao.tddl.executor.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.taobao.tddl.common.jdbc.IConnection;
import com.taobao.tddl.common.jdbc.Parameters;
import com.taobao.tddl.common.model.SqlMetaData;
import com.taobao.tddl.common.properties.ParamManager;
import com.taobao.tddl.common.utils.logger.Logger;
import com.taobao.tddl.common.utils.logger.LoggerFactory;
import com.taobao.tddl.executor.repo.RepositoryDefault;
import com.taobao.tddl.executor.spi.IRepository;
import com.taobao.tddl.executor.spi.ITable;
import com.taobao.tddl.executor.spi.ITempTable;
import com.taobao.tddl.executor.spi.ITransaction;
import com.taobao.tddl.statistics.SQLRecorder;
import com.taobao.tddl.statistics.SQLTracer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 一次执行过程中的上下文
 *
 * @author whisper
 */
public class ExecutionContext {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionContext.class);

    /**
     * 当前运行时的存储对象
     */
    private IRepository currentRepository = new RepositoryDefault();

    /**
     * 是否自动关闭结果集。目前这个东西已经基本无效。除了在update等查询中有使用
     */
    private boolean closeResultSet;
    /**
     * 当前事务
     */
    private ITransaction transaction;

    private Map<String, Object> extraCmds = new HashMap();

    private ParamManager paramManager = new ParamManager(extraCmds);
    private Parameters params = null;

    private String isolation = null;

    private ExecutorService concurrentService;

    private boolean autoCommit = true;

    private int txIsolation = -1;

    private String groupHint = null;

    private int autoGeneratedKeys = -1;

    private int[] columnIndexes = null;

    private String[] columnNames = null;

    private int resultSetType = -1;

    private int resultSetConcurrency = -1;

    private int resultSetHoldability = -1;

    private volatile Cache<String, ITempTable> tempTables = CacheBuilder.newBuilder().build();

    private IConnection connection = null;

    private InputStream localInFileInputStream = null;

    private String sqlMode = null;

    private String sql = null;

    private String encoding = null;

    private String appName;

    private SQLRecorder recorder;

    private SQLTracer tracer;
    // 为eagleEye埋点统计sql
    private SqlMetaData sqlMetaData;

    private boolean enableTrace;

    public ExecutionContext() {

    }

    public Cache<String, ITempTable> getTempTables() {
        return tempTables;
    }

    public void setTempTables(Cache<String, ITempTable> tempTables) {
        this.tempTables = tempTables;
    }

    public IRepository getCurrentRepository() {
        return currentRepository;
    }

    public void setCurrentRepository(IRepository currentRepository) {
        this.currentRepository = currentRepository;
    }

    public boolean isCloseResultSet() {
        return closeResultSet;
    }

    public void setCloseResultSet(boolean closeResultSet) {
        this.closeResultSet = closeResultSet;
    }

    public ITransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(ITransaction transaction) {
        this.transaction = transaction;

    }

    public Map<String, Object> getExtraCmds() {
        return extraCmds;
    }

    public void setExtraCmds(Map<String, Object> extraCmds) {
        this.extraCmds = extraCmds;

        this.paramManager = new ParamManager(extraCmds);
    }

    public Parameters getParams() {
        return params;
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public String getIsolation() {
        return isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public ExecutorService getExecutorService() {
        return this.concurrentService;
    }

    public void setExecutorService(ExecutorService concurrentService) {
        this.concurrentService = concurrentService;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;

    }

    public String getGroupHint() {
        return groupHint;
    }

    public void setGroupHint(String groupHint) {
        this.groupHint = groupHint;
    }

    public int getAutoGeneratedKeys() {
        return autoGeneratedKeys;
    }

    public void setAutoGeneratedKeys(int autoGeneratedKeys) {
        this.autoGeneratedKeys = autoGeneratedKeys;
    }

    public int[] getColumnIndexes() {
        return columnIndexes;
    }

    public void setColumnIndexes(int[] columnIndexes) {
        this.columnIndexes = columnIndexes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public int getResultSetType() {
        return resultSetType;
    }

    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
    }

    public int getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    public void setResultSetConcurrency(int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    public int getResultSetHoldability() {
        return resultSetHoldability;
    }

    public void setResultSetHoldability(int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
    }

    public ParamManager getParamManager() {
        return this.paramManager;
    }

    public void setParamManager(ParamManager pm) {
        this.paramManager = pm;
    }

    public void cleanTempTables() {
        for (ITable tempTable : getTempTables().asMap().values()) {
            try {
                tempTable.close();
            } catch (Throwable e) {
                logger.warn("temp table close failed", e);
            }
        }
    }

    public IConnection getConnection() {
        return connection;
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }

    public int getTxIsolation() {
        return txIsolation;
    }

    public void setTxIsolation(int txIsolation) {
        this.txIsolation = txIsolation;
    }

    public InputStream getLocalInfileInputStream() {
        return this.localInFileInputStream;
    }

    public void setLocalInfileInputStream(InputStream stream) {
        this.localInFileInputStream = stream;
    }

    public String getSqlMode() {
        return sqlMode;
    }

    public void setSqlMode(String sqlMode) {
        this.sqlMode = sqlMode;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public SQLRecorder getRecorder() {
        return this.recorder;

    }

    public void setRecorder(SQLRecorder recorder) {
        this.recorder = recorder;
    }

    public SQLTracer getTracer() {
        return this.tracer;
    }

    public void setTracer(SQLTracer tracer) {
        this.tracer = tracer;
    }

    public boolean isEnableTrace() {
        return this.enableTrace;
    }

    public void setEnableTrace(boolean enableTrace) {
        this.enableTrace = enableTrace;
    }

    public SqlMetaData getSqlMetaData() {
        return sqlMetaData;
    }

    public void setSqlMetaData(SqlMetaData sqlMetaData) {
        this.sqlMetaData = sqlMetaData;
    }

}
