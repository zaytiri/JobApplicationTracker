package personal.zaytiri.jobtracker.api.mappers.base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class Mapper<E, M> implements IMapper<E, M> {

    protected abstract String getTablePrefix();

    protected String getFormattedName(boolean mixedResult, String columnName) {
        if (mixedResult) {
            return getTablePrefix() + columnName;
        }
        return columnName;
    }

    protected Boolean getRowBooleanValue(Map<String, String> row, boolean mixedResult, String columnName) {
        String value = getRowValue(row, mixedResult, columnName);
        if (value == null) {
            return false;
        }
        return Integer.parseInt(value) != 0;
    }

    protected LocalDateTime getRowDateValue(Map<String, String> row, boolean mixedResult, String columnName) {
        String value = getRowValue(row, mixedResult, columnName);
        if (value == null) {
            return LocalDateTime.MIN;
        }
        return LocalDateTime.parse(value);
    }

    protected int getRowIntValue(Map<String, String> row, boolean mixedResult, String columnName) {
        String value = getRowValue(row, mixedResult, columnName);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    protected String getRowStringValue(Map<String, String> row, boolean mixedResult, String columnName) {
        String value = getRowValue(row, mixedResult, columnName);
        if (value == null) {
            return "";
        }
        return value;
    }

    private String getRowValue(Map<String, String> row, boolean mixedResult, String columnName) {
        return row.get(getFormattedName(mixedResult, columnName));
    }
}
