package de.perschon.shoppinglistbackend.shoppinglists

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class ShoppingListService : KoinComponent {

    private val collection by inject<CoroutineCollection<ShoppingList>>()

    suspend fun getAllShoppingLists(): List<ShoppingList> = collection.find().toList()

    suspend fun getShoppingList(id: String): ShoppingList? = collection.findOne(ShoppingList::id eq id)

    suspend fun create(list: ShoppingList): ShoppingList {
        return list.also {
            collection.insertOne(it)
        }
    }

}
