package de.perschon.shoppinglistbackend.graphql

import de.perschon.shoppinglistbackend.products.ProductService
import graphql.GraphQL
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import kotlinx.coroutines.runBlocking
import org.koin.standalone.StandAloneContext
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions

fun buildGraphQL(): GraphQL {
    val productService = StandAloneContext.getKoin().koinContext.get<ProductService>()
    val query = StandAloneContext.getKoin().koinContext.get<Query>()

    val schemaString = """
        type Query {
            hello: String!
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
        .type("Query") { builder ->
            builder.dataFetcher("hello", StaticDataFetcher("world"))

            Query::class.declaredFunctions.forEach {
                builder.dataFetcher(it.name) { env ->
                    runBlocking {
                        suspendCoroutine<Any?> { cont ->
                            val params = it.parameters
                                .sortedBy(KParameter::index)
                                .subList(1, it.parameters.size)
                                .map { env.arguments[it.name] }
                                .toTypedArray()

                            it.call(query, cont, *params)
                        }
                    }
                }
            }

            builder.dataFetcher("product") { env ->
                runBlocking {
                    productService.getById(env.arguments["id"] as String)
                }
            }
        }
        .build()

    val schemaGenerator = SchemaGenerator()
    val graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)

    return GraphQL.newGraphQL(graphQLSchema).build()
}

data class GraphQLRequest(
    val query: String
)

data class GraphQLResponse(
    val data: Any
)
