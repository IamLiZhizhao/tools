package com.lzz.tools.helper;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class ListHelper {
    public ListHelper() {
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isObjectNullOrEmpty(Collection collection) {
        return null == collection || collection.isEmpty() || collection.stream().allMatch(StringUtils::isEmpty);
    }

    public static <T> T first(List<T> list) {
        return !isNullOrEmpty(list) ? list.get(0) : null;
    }

    public static <T> void remove(List<T> list, T item) {
        if (list != null && list.size() != 0 && item != null) {
            Iterator it = list.iterator();

            while(it.hasNext()) {
                if (it.next() == item) {
                    it.remove();
                }
            }

        }
    }
}
