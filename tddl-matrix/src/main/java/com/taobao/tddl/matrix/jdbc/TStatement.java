package com.taobao.tddl.matrix.jdbc;

import com.taobao.tddl.common.exception.TddlNestableRuntimeException;
import com.taobao.tddl.common.jdbc.IStatement;
import com.taobao.tddl.common.jdbc.Parameters;
import com.taobao.tddl.common.properties.ConnectionProperties;
import com.taobao.tddl.common.utils.GeneralUtil;
import com.taobao.tddl.common.utils.logger.Logger;
import com.taobao.tddl.common.utils.logger.LoggerFactory;
import com.taobao.tddl.executor.common.ExecutionContext;
import com.taobao.tddl.executor.cursor.ResultCursor;
import com.taobao.tddl.executor.cursor.impl.ArrayResultCursor;
import com.taobao.tddl.optimizer.OptimizerContext;
import com.taobao.tddl.optimizer.core.datatype.DataType;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengshi.sunmengshi 2013-11-22 下午3:26:28
 * @since 5.0.0
 */
public class TStatement implements IStatement {

    private static final Logger log = LoggerFactory.getLogger(TStatement.class);
    private final String appName;
    protected String sql;
    protected TDataSource ds;
    protected TConnection conn;
    protected ExecutionContext executionContext = null;
    /**
     * 更新计数，如果执行了多次，那么这个值只会返回最后一次执行的结果。 如果是一个query，那么返回的数据应该是-1
     */
    protected int updateCount;
    /**
     * 经过计算后的结果集，允许使用 getResult函数调用. 一个statement只允许有一个结果集
     */
    protected ResultSet currentResultSet;
    /**
     * 返回generated keys
     */
    protected ResultSet generatedKeysResults;
    protected Map<String, Object> extraCmd = new HashMap<String, Object>(4);
    /**
     * 当前statment 是否是关闭的
     */
    protected boolean closed;
    protected int maxFieldSize;
    protected int maxRows;
    protected int queryTimeOut;
    protected int direction;
    protected List<String> batchedArgs;
    protected int resultSetType = -1;
    protected int resultSetConcurrency = -1;
    protected int resultSetHoldability = -1;
    protected InputStream localInFileInputStream = null;
    /**
     * 貌似是只有存储过程中会出现多结果集 因此不支持
     */
    protected boolean moreResults;

    public TStatement(TDataSource ds, TConnection tConnection, ExecutionContext executionContext) {
        this.ds = ds;
        this.conn = tConnection;
        this.executionContext = executionContext;
        this.appName = executionContext.getAppName();
    }

    public TStatement(TDataSource ds, TConnection tConnection, String sql, ExecutionContext executionContext) {
        this.ds = ds;
        this.conn = tConnection;
        this.sql = sql;
        this.executionContext = executionContext;
        this.appName = executionContext.getAppName();
    }

    protected void checkClosed() throws SQLException {
        if (closed) {
            throw new SQLException("No operations allowed after statement closed.");
        }
    }

    // jdbc规范: 返回true表示executeQuery，false表示executeUpdate
    @Override
    public boolean execute(String sql) throws SQLException {
        return executeInternal(sql, -1, null, null);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return executeInternal(sql, autoGeneratedKeys, null, null);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return executeInternal(sql, -1, columnIndexes, null);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return executeInternal(sql, -1, null, columnNames);
    }

    // jdbc规范: 返回true表示executeQuery，false表示executeUpdate
    protected boolean executeInternal(String sql, int autoGeneratedKeys, int[] columnIndexes, String[] columnNames)
            throws SQLException {
        OptimizerContext.setContext(this.ds.getConfigHolder().getOptimizerContext());
        boolean write = conn.isWrite(sql);
        if (write) {
            if (autoGeneratedKeys == -1 && columnIndexes == null && columnNames == null) {
                this.updateCount = executeUpdate(sql);
            } else if (autoGeneratedKeys != -1) {
                this.updateCount = executeUpdate(sql, autoGeneratedKeys);
            } else if (columnIndexes != null) {
                this.updateCount = executeUpdate(sql, columnIndexes);
            } else if (columnNames != null) {
                this.updateCount = executeUpdate(sql, columnNames);
            } else {
                this.updateCount = executeUpdate(sql);
            }
            return false;
        } else {
            executeQuery(sql);
            return true;
        }
    }

    /*
     * ========================================================================
     * executeQuery 查询逻辑 这里按照mysql
     * connection逻辑，调用connection的executeSQL方法返回resultset Connection 的 execSQL方法
     * ======================================================================
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        checkClosed();
        ensureResultSetIsEmpty();
        currentResultSet = this.conn.executeSQL(sql, null, this, extraCmd, this.executionContext);
        this.moreResults = false;
        return currentResultSet;
    }

    /*
     * ========================================================================
     * executeUpdate逻辑 这里按照mysql
     * connection逻辑，调用connection的executeSQL方法返回resultset
     * ,然后根据resultset获得affertrows Connection 的 execSQL方法
     * ======================================================================
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {
        return executeUpdateInternal(sql, -1, null, null);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return executeUpdateInternal(sql, autoGeneratedKeys, null, null);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return executeUpdateInternal(sql, -1, columnIndexes, null);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return executeUpdateInternal(sql, -1, null, columnNames);
    }

    protected int executeUpdateInternal(String sql, int autoGeneratedKeys, int[] columnIndexes, String[] columnNames)
            throws SQLException {
        checkClosed();
        ensureResultSetIsEmpty();
        executionContext.setAutoGeneratedKeys(autoGeneratedKeys);
        executionContext.setColumnIndexes(columnIndexes);
        executionContext.setColumnNames(columnNames);
        currentResultSet = this.conn.executeSQL(sql, null, this, extraCmd, this.executionContext);
        this.moreResults = false;
        return ((TResultSet) currentResultSet).getAffectRows();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new UnsupportedOperationException("addBatch(String sql) is not supported, please use addBatch() in PreparedStatement");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new UnsupportedOperationException("executeBatch");
    }

    @Override
    public void clearBatch() throws SQLException {
        checkClosed();
        throw new UnsupportedOperationException("clearBatch() is not supported, please use clearBatch() in PreparedStatement");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public void close() throws SQLException {
        close(true);
    }

    void close(boolean removeThis) throws SQLException {
        if (closed) {
            return;
        }
        try {
            if (currentResultSet != null) {
                currentResultSet.close();
            }

            if (removeThis) {
                conn.removeStatement(this);
            }
        } catch (Exception e) {
            throw new TddlNestableRuntimeException(e);
        } finally {
            currentResultSet = null;
        }
        closed = true;
    }

    /**
     * 如果新建了查询，那么上一次查询的结果集应该被显示的关闭掉。这才是符合jdbc规范的
     *
     * @throws SQLException
     */
    protected void ensureResultSetIsEmpty() throws SQLException {
        if (currentResultSet != null) {
            // log.debug("result set is not null,close current result set");
            try {
                currentResultSet.close();
            } catch (SQLException e) {
                log.error("exception on close last result set . can do nothing..", e);
            } finally {
                // 最终要显示的关闭它
                currentResultSet = null;
            }
        }

        if (generatedKeysResults != null) {
            try {
                generatedKeysResults.close();
            } catch (SQLException e) {
                log.error("exception on close last result set . can do nothing..", e);
            } finally {
                // 最终要显示的关闭它
                generatedKeysResults = null;
            }
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.maxFieldSize;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        this.maxFieldSize = max;

    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.maxRows;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        this.maxRows = max;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return currentResultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return updateCount;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.conn;
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.queryTimeOut;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        this.queryTimeOut = seconds;

    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return (T) this;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.getClass().isAssignableFrom(iface);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.direction;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        this.direction = direction;

    }

    @Override
    public int getFetchSize() throws SQLException {
        return (int) GeneralUtil.getExtraCmdLong(extraCmd, ConnectionProperties.FETCH_SIZE, 0L);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        extraCmd.put(ConnectionProperties.FETCH_SIZE, rows);
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return resultSetConcurrency;
    }

    public void setResultSetConcurrency(int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
        this.executionContext.setResultSetConcurrency(resultSetConcurrency);
    }

    @Override
    public int getResultSetType() throws SQLException {
        return resultSetType;
    }

    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
        this.executionContext.setResultSetType(resultSetType);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return resultSetHoldability;
    }

    public void setResultSetHoldability(int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
        this.executionContext.setResultSetHoldability(resultSetHoldability);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return moreResults;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return moreResults;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        if (this.executionContext == null
                || this.executionContext.getAutoGeneratedKeys() != java.sql.Statement.RETURN_GENERATED_KEYS) {
            throw new SQLException("Generated keys not requested. You need to specify Statement.RETURN_GENERATED_KEYS to Statement.executeUpdate() or Connection.prepareStatement().");
        }

        ArrayResultCursor result = new ArrayResultCursor("GENERATED_KEYS", executionContext);
        result.addColumn("GENERATED_KEY", DataType.LongType);
        result.initMeta();

        List<Long> generatedKeys = this.conn.getGeneratedKeys();
        if (generatedKeys == null || generatedKeys.isEmpty()) {
            long last = this.conn.getLastInsertId();
            Parameters params = this.executionContext.getParams();
            if (params.isBatch()) {
                int size = this.executionContext.getParams().getBatchSize();
                for (int i = 1; i <= size; i++) {
                    result.addRow(new Object[]{last - size + i});
                }
            } else {
                result.addRow(new Object[]{last});
            }
        } else {
            // 如果是直连下推模式，直接获取mysql返回的keys进行返回
            for (Long id : generatedKeys) {
                result.addRow(new Object[]{id});
            }
        }

        generatedKeysResults = new TResultSet(new ResultCursor(result, this.executionContext));
        return generatedKeysResults;
    }

    @Override
    public void cancel() throws SQLException {
        this.conn.cancelQuery();
    }

    /*---------------------后面是未实现的方法------------------------------*/

    @Override
    public boolean isPoolable() throws SQLException {
        throw new SQLException("not support exception");
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        throw new SQLException("not support exception");
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new UnsupportedOperationException("setEscapeProcessing");
    }

    @Override
    public void setCursorName(String cursorName) throws SQLException {
        throw new UnsupportedOperationException("setCursorName");
    }

    @Override
    public InputStream getLocalInfileInputStream() {
        return this.executionContext.getLocalInfileInputStream();
    }

    @Override
    public void setLocalInfileInputStream(InputStream stream) {
        this.executionContext.setLocalInfileInputStream(stream);
    }

    @Override
    public String getAppName() {
        return this.appName;
    }
}
