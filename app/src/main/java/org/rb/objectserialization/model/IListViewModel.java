package org.rb.objectserialization.model;

import java.util.List;

public interface IListViewModel<V,T> {

    /**
     * T is model item type.
     * V is item's model view type used in view presentation (f.e., in ListView)
     * <pre>
     * Example:
     * We have List&lt;T>  items.
     * in listView we want to display just some fields and in specific format.
     * </pre>
     * The viewModel method converts List&lt;T> to List&lt;V>.
     * @param items to be converted to other type.
     * @return
     */
    List<V> viewModel(List<T> items);
}
