package org.example.response;

import java.util.List;

/**
 *suitable for all entities that do not scope FetchType.LAZY
 * @param <T>
 */
public class ItemsResponse<T>{
    private final int itemsCount;
    private final List<T> items;

    public ItemsResponse(List<T> items) {
        this.itemsCount = items.size();
        this.items = items;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public List<T> getItems() {
        return items;
    }
}
