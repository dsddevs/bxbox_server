package dsd.bxbox.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Service
public class DbCreator implements IDbCreator  {

    private final DataSource dataSource;
    @Override
    public boolean isTableExists(String tableName) {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            return metaData.getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public void createTable(String tableName, String[] headers, String[] firstRows) {
        if (!isTableExists(tableName)) {
            StringBuilder table = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
            columnAndDataTypeHandler(headers, firstRows, table);
            table = new StringBuilder(table.substring(0, table.length() - 2) + ")");
            connectDbToCreate(table);
        } else {
            throw new RuntimeException("Table already exists in db!");
        }
    }
    @Override
    public void insertData(String tableName, String[] headers, BufferedReader reader, String[] firstRows) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        for (String header : headers) sql.append("`").append(header.trim()).append("`").append(",");
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ") VALUES (");
        sql.append("?,".repeat(headers.length));
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ")");
        connectDbToInsert(sql, firstRowHandler(reader, firstRows));
    }
    private List<String[]> firstRowHandler(BufferedReader reader, String[] firstRows) {
        List<String[]> rows = new ArrayList<>(Collections.singleton(firstRows));
        try {
            String line;
            while ((line = reader.readLine()) != null) rows.add(line.split(","));
            return rows;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private void columnAndDataTypeHandler(String[] headers, String[] firstRows, StringBuilder table) {
        for (int i = 0; i < headers.length; i++) {
            String columnName = headers[i].trim().replaceAll("[^\\w.\\-_а-яА-Я]", "");
            String columnType = determineColumnType(firstRows[i]);
            table.append("`").append(columnName).append("`").append(" ").append(columnType).append(", ");
        }
    }
    private void connectDbToCreate(StringBuilder table) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(table.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table: " + e.getMessage(), e);
        }
    }
    private void connectDbToInsert(StringBuilder sql, List<String[]> rows) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql.toString())) {
            for (String[] row : rows) {
                parseValue(row, statement);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String determineColumnType(String value) {
        if (value.matches("\\d+")) return "BIGINT";
        else if (value.matches("\\d+.\\d+")) return "DECIMAL(19,3)";
        else if (value.matches("\\d{2}.\\d{2}.\\d{4}")) return "DATE";
        else return "VARCHAR(255)";
    }
    private void parseValue(String[] values, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            String value = values[i].replaceAll(",", ".");
            value = value.replaceAll("[^\\w._а-яА-Я]", "").trim();
            if (values.length != statement.getParameterMetaData().getParameterCount()) continue;
            if (value.matches("\\d{2}.\\d{2}.\\d{4}")) {
                String[] dateParts = value.split("\\.");
                value = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            }
            statement.setString(i + 1, value);
        }
    }

}
