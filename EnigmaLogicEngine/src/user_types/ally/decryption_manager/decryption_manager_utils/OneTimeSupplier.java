package user_types.ally.decryption_manager.decryption_manager_utils;

import java.util.function.Supplier;

public class OneTimeSupplier<T> implements Supplier<T> {
    private boolean isSupplied = false;
    private final T item;

    public OneTimeSupplier(T item){
        this.item = item;
    }

    @Override
    public T get() {
        if (!isSupplied) {
            isSupplied = true;
            return item;
        }
        return null;
    }
}
