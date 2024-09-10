package dsd.bxbox.db;

import java.io.BufferedReader;

public interface IDbCreator {
    boolean isTableExists(String tableName);

    void createTable(String tableName, String[] headers, String[] firstRow);

    void insertData(String tableName, String[] headers, BufferedReader reader, String[] firstRow);
}
