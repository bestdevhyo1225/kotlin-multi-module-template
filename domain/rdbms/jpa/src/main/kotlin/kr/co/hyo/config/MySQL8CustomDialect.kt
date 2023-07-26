package kr.co.hyo.config

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.dialect.DatabaseVersion
import org.hibernate.dialect.MySQLDialect
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.type.BasicTypeRegistry
import org.hibernate.type.StandardBasicTypes.BIG_DECIMAL
import org.hibernate.type.spi.TypeConfiguration

class MySQL8CustomDialect : MySQLDialect(DatabaseVersion.make(8)) {

    override fun initializeFunctionRegistry(functionContributions: FunctionContributions) {
        super.initializeFunctionRegistry(functionContributions)
        val functionRegistry: SqmFunctionRegistry = functionContributions.functionRegistry
        val typeConfiguration: TypeConfiguration = functionContributions.typeConfiguration
        val basicTypeRegistry: BasicTypeRegistry = typeConfiguration.basicTypeRegistry
        functionRegistry.registerPattern(
            "match_against",
            "match(?1) against (?2 in boolean mode)",
            basicTypeRegistry.resolve(BIG_DECIMAL),
        )
    }
}
