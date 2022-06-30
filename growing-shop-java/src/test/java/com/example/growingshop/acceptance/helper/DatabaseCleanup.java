package com.example.growingshop.acceptance.helper;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ActiveProfiles("acceptance")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + wrapReservedWord(tableName)).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + wrapReservedWord(tableName) + " ALTER COLUMN ID RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private String wrapReservedWord(String tableName) {
        if (reservedWords().contains(tableName.toUpperCase())) {
            return "\"" + tableName + "\"";
        }

        return tableName;
    }

    private List<String> reservedWords() {
        return Arrays.asList(
                "ABORT", "ALL", "ALLOCATE", "ANALYSE", "ANALYZE", "AND", "ANY", "AS", "ASC",

                "BETWEEN", "BINARY", "BIT", "BOTH",

                "CASE", "CAST", "CHAR", "CHARACTER", "CHECK", "CLUSTER", "COALESCE", "COLLATE", "COLLATION", "COLUMN",
                "CONSTRAINT", "COPY", "CROSS", "CURRENT", "CURRENT_CATALOG", "CURRENT_DATE", "CURRENT_DB",
                "CURRENT_SCHEMA", "CURRENT_SID", "CURRENT_TIME", "CURRENT_TIMESTAMP",
                "CURRENT_USER", "CURRENT_USERID", "CURRENT_USEROID",

                "DEALLOCATE", "DEC", "DECIMAL", "DECODE", "DEFAULT", "DESC", "DISTINCT", "DISTRIBUTE", "DO",

                "ELSE", "END", "EXCEPT", "EXCLUDE", "EXISTS", "EXPLAIN", "EXPRESS", "EXTEND", "EXTERNAL", "EXTRACT",

                "FALSE", "FIRST", "FLOAT", "FOLLOWING", "FOR", "FOREIGN", "FROM", "FULL", "FUNCTION",

                "GENSTATS", "GLOBAL", "GROUP",

                "HAVING",

                "IDENTIFIER_CASE", "ILIKE", "IN", "INDEX", "INITIALLY",
                "INNER", "INOUT", "INTERSECT", "INTERVAL", "INTO",

                "LEADING", "LEFT", "LIKE", "LIMIT", "LOAD", "LOCAL", "LOCK",

                "MINUS", "MOVE",

                "NATURAL", "NCHAR", "NEW", "NOT", "NOTNULL", "NULL", "NULLS", "NUMERIC", "NVL", "NVL2",

                "OFF", "OFFSET", "OLD", "ON", "ONLINE", "ONLY", "OR",
                "ORDER", "OTHERS", "OUT", "OUTER", "OVER", "OVERLAPS",

                "PARTITION", "POSITION", "PRECEDING", "PRECISION", "PRESERVE", "PRIMARY",

                "RESET", "RESET", "REUSE", "REUSE", "RIGHT", "ROWS",

                "SELECT", "SESSION_USER", "SETOF", "SHOW", "SOME",

                "TABLE", "THEN", "TIES", "TIME", "TIMESTAMP", "TO", "TRAILING",
                "TRANSACTION", "TRIGGER", "TRIM", "TRUE",

                "UNBOUNDED", "UNION", "UNIQUE", "USER", "USING",

                "VACUUM", "VARCHAR", "VERBOSE", "VERSION", "VIEW",

                "WHEN", "WHERE", "WITH", "WRITE"
        );
    }
}
