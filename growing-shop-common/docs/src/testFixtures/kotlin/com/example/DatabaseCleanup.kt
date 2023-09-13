package com.example

import com.google.common.base.CaseFormat
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
@ActiveProfiles("acceptance")
open class DatabaseCleanup: InitializingBean {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var tableNames: List<String>

    override fun afterPropertiesSet() {
        tableNames = entityManager.metamodel
            .entities
            .filter { e -> e.javaType.getAnnotation(Entity::class.java) != null }
            .map { e ->
                CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    e.name.replace("`", "")
                )
            }
    }

    @Transactional
    open fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        for (tableName in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + wrapReservedWord(tableName)).executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE " + wrapReservedWord(tableName) + " ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate()
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }

    private fun wrapReservedWord(tableName: String): String {
        return if (reservedWords().contains(tableName.uppercase(Locale.getDefault()))) {
            "\"" + tableName + "\""
        } else tableName
    }

    private fun reservedWords(): List<String> {
        return mutableListOf(
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
        )
    }
}
