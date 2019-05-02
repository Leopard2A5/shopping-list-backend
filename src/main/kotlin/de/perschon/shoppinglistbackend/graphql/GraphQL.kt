package de.perschon.shoppinglistbackend.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import de.perschon.shoppinglistbackend.config.objectMapper
import graphql.GraphQL
import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import kotlinx.coroutines.runBlocking
import org.koin.standalone.StandAloneContext
import java.io.File
import java.io.InputStreamReader
import java.util.function.UnaryOperator
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaType

private val om = objectMapper()

fun buildGraphQL(): GraphQL {
    val query = StandAloneContext.getKoin().koinContext.get<Query>()
    val mutation = StandAloneContext.getKoin().koinContext.get<Mutation>()

    val schemaString = File(object{}.javaClass.classLoader.getResource("schema").toURI()).let { file ->
        file.list()
            .map { File(file, it) }
            .map { it.inputStream().reader().use(InputStreamReader::readText) }
            .joinToString(separator = "\n")
    }

    val schemaParser = SchemaParser()
    val typeDefinitionRegistry = schemaParser.parse(schemaString)

    val runtimeWiring = newRuntimeWiring()
        .type("Query", resolveAllFunctions(query))
        .type("Mutation", resolveAllFunctions(mutation))
        .build()

    val schemaGenerator = SchemaGenerator()
    val graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)

    return GraphQL.newGraphQL(graphQLSchema).build()
}

fun resolveAllFunctions(instance: Any): UnaryOperator<TypeRuntimeWiring.Builder> {
    return UnaryOperator { builder ->
        instance::class.declaredFunctions.forEach { function ->
            val functionArguments = function.parameters
                .sortedBy(KParameter::index)
                .subList(1, function.parameters.size)

            builder.dataFetcher(function.name) { env ->
                runBlocking {
                    suspendCoroutine<Any?> { cont ->
                        val params = functionArguments
                            .map(env::getArgument)
                            .toTypedArray()
                        function.call(instance, *params, cont)
                    }
                }
            }
        }

        builder
    }
}

fun DataFetchingEnvironment.getArgument(
    param: KParameter
): Any? {
    return when(val tmp = arguments[param.name]) {
        tmp is Map<*, *> -> om.convertValue(tmp, param.type.javaType as Class<*>)
        else -> tmp
    }
}

data class GraphQLRequest(
    val query: String
)

data class GraphQLResponse(
    val data: Any
)
