package service.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public interface SqlGenerator {
    Integer COLUMN_NAMES_INDEX = 0;
    String TABLE_NAME = "#TABLE_NAME";
    String COLUMN_NAMES = "#COLUMN_NAMES";
    String COMMA = ",";
    String VALUES = "#VALUES";
    String OPENING_BRACKET = "(";
    String CLOSING_BRACKET = ")";
    String EMPTY = "";
    String UPDATE_PAIRS = "#UPDATE_PAIRS";
    String PREDICATES = "#PREDICATES";
    String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;
    String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET " + UPDATE_PAIRS + " WHERE " + PREDICATES;
    String AND = " and ";
    String SEMICOLON = ";";
    String EQUALZ = "=";
    String ASTERISK = "*"; // the column which has this will be used in the predicate
    String GREATER_THAN = ">";
    String QUESTION_MARK = "?";
    String AMPERSAND = "&";

    String generate(String tableName, Map<Integer, List<String>> rowsMap);

    default void removeFirstRow(Map<Integer, List<String>> rowsMap) {
        rowsMap.remove(0);
    }


    default List<String> extractColumnNames(Map<Integer, List<String>> rowsMap) {
        return requireNonNull(rowsMap.get(COLUMN_NAMES_INDEX));
    }

    default boolean columnNeedsAdapting(String columnName){
        return columnName.contains(GREATER_THAN);
    }
    default Map<String ,String> extractColumnTranslation(String adaptedColumnName){
        Map<String,String> toReturn = new HashMap<>();
        String adaptions = substringAfter(adaptedColumnName,GREATER_THAN);
        String[] parts = adaptions.split(AMPERSAND);
        stream(parts).forEach(s -> toReturn.put(s.split(EQUALZ)[0],s.split(EQUALZ)[1]));
        return toReturn;
    }
    default String pureColumnName(String adaptedColumnName){
        return substringBefore(adaptedColumnName,GREATER_THAN);
    }
    default boolean ignoreColumn(String columnName){
        return columnName.contains(OPENING_BRACKET) && columnName.contains(CLOSING_BRACKET);
    }
}
