package com.sppProject.app.model.api_integration.fetchers
import com.sppProject.app.model.api_integration.ApiFetcher
import com.sppProject.app.model.api_integration.api_service.ItemApiService
import com.sppProject.app.model.data.data_class.Item


class ItemFetcher (
    private val itemApiFetcher: ItemApiService
){
    private val apiFetcher = ApiFetcher<Item>(itemApiFetcher)

    suspend fun fetchItems(): List<Item> {
        return apiFetcher.handleApiCallList { itemApiFetcher.getAllItems() }
    }

    suspend fun createItem(compId: Long, newItem: Item): Item {
        return apiFetcher.handleApiCallSingle { itemApiFetcher.addItem(compId, newItem) }
    }

    suspend fun getItemById(id: Long): Item {
        return apiFetcher.handleApiCallSingle { itemApiFetcher.getItemById(id) }
    }

    suspend fun fetchItemsByCompanyId(compId: Long): List<Item> {
        return apiFetcher.handleApiCallList { itemApiFetcher.getItemsByCompany(compId) }

    }



}