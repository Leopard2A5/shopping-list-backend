package de.perschon.shoppinglistbackend

import graphql.GraphQL
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser

fun buildGraphQL(): GraphQL {
    val schemaString = """
        type Query {
            hello: String!
        }
    """.trimIndent()

    val schemaParser = SchemaParser()
    val typeDefinitionRegistry = schemaParser.parse(schemaString)

    val runtimeWiring = newRuntimeWiring()
        .type("Query") { builder ->
            builder.dataFetcher("hello", StaticDataFetcher("world"))
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
