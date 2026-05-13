package top.jpower.core.dbs.support.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Boolean.class)
public class YesOrNoEnumTypeHandler implements TypeHandler<Boolean> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, willVal(parameter));
    }

    private String willVal(Boolean parameter) {
        if (parameter == null) {
            return null;
        }
        return parameter ? "yes" : "no";
    }

    @Override
    public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return "yes".equalsIgnoreCase(columnValue);
    }

    @Override
    public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return "yes".equalsIgnoreCase(columnValue);
    }

    @Override
    public Boolean getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return "yes".equalsIgnoreCase(columnValue);
    }
}