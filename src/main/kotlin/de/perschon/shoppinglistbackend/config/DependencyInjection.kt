package de.perschon.shoppinglistbackend.config

import com.uchuhimo.konf.Config
import de.perschon.shoppinglistbackend.graphql.Query
import de.perschon.shoppinglistbackend.products.ProductController
import de.perschon.shoppinglistbackend.products.ProductService
import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListController
import de.perschon.shoppinglistbackend.shoppinglists.ShoppingListService
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun appModule(config: Config) = module {
    single { KMongo.createClient(config[Mongo.connectionString]).coroutine }
    single { get<CoroutineClient>().getDatabase(config[Mongo.databaseName]) }

    single { ProductService(get()) }
    single { ProductController(get(), get()) }

    single { ShoppingListService() }
    single { ShoppingListController(get()) }

    single { Query() }
}

fun initDependencyInjection(config: Config) {
    startKoin(listOf(appModule(config)))
}
