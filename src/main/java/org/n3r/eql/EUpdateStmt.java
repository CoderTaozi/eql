package org.n3r.eql;

import org.n3r.eql.ex.EqlExecuteException;
import org.n3r.eql.map.EqlRun;
import org.n3r.eql.param.EqlParamsBinder;
import org.n3r.eql.util.Closes;
import org.slf4j.Logger;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EUpdateStmt implements Closeable, EStmt {
    private boolean autoCommit = true;
    private PreparedStatement preparedStatement;
    private EqlRun eqlRun;
    private Logger logger;
    private EqlTran eqlTran;
    private Object[] params;
    private String sqlClassPath;

    @Override
    public void close() {

    }

    public int update() {
        return update(params);
    }

    public int update(Object... params) {
        eqlRun.setParams(params);
        new EqlParamsBinder().prepareBindParams(false, eqlRun);
        eqlRun.bindParams(preparedStatement, sqlClassPath);
        int ret;
        try {
            ret = preparedStatement.executeUpdate();
            if (autoCommit && eqlTran != null) eqlTran.commit();
            return ret;
        } catch (SQLException e) {
            throw new EqlExecuteException("executeUpdate failed", e);
        }

    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void commit() {
        if (eqlTran != null) eqlTran.commit();
    }

    public void rollback() {
        if (eqlTran != null) eqlTran.rollback();
    }

    @Override
    public void setPreparedStatment(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public void setEqlRun(EqlRun eqlRun) {
        this.eqlRun = eqlRun;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void closeStmt() {
        Closes.closeQuietly(preparedStatement);
        preparedStatement = null;
    }

    @Override
    public void setEqlTran(EqlTran eqlTran) {
        this.eqlTran = eqlTran;
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    @Override
    public void setSqlClassPath(String sqlClassPath) {
        this.sqlClassPath = sqlClassPath;
    }

    @Override
    public void params(Object[] params) {
        this.params = params;
    }


}
