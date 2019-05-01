package de.perschon.shoppinglistbackend.graphql

import de.perschon.shoppinglistbackend.products.ProductService
import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import kotlinx.coroutines.runBlocking
import org.koin.standalone.StandAloneContext
import java.util.function.UnaryOperator
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions

fun buildGraphQL(): GraphQL {
    val productService = StandAloneContext.getKoin().koinContext.get<ProductService>()
    val query = StandAloneContext.getKoin().koinContext.get<Query>()

    val schemaString = """
        type Query {
            products: [Product!]!
            product(id: String!): Product
        }

        type Product {
            id: String!
            name: String!
        }
    """.trimIndent()

    val schemaParser = SchemaParser()
    val typeDefinitionRegistry = schemaParser.parse(schemaString)

    val runtimeWiring = newRuntimeWiring()
        .type("Query", resolveAllFunctions(query))
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
                            .map { env.arguments[it.name] }
                            .toTypedArray()

                        function.call(instance, *params, cont)
                    }
                }
            }
        }

        builder
    }
}

data class GraphQLRequest(
    val query: String
)

data class GraphQLResponse(
    val data: Any
)
