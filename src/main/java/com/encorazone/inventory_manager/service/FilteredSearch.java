package com.encorazone.inventory_manager.service;

/**
 * This class represents the first try to sorting and filtering the data.
 * Should have deleted it, but le agarré cariño. Not gonna explain the method though.
 * The method explain themselves
 */
public class FilteredSearch {
    public Number attributesCounter(String name, String category, Number stock) {
        int num = 0;
        if (!name.isEmpty()) {
            num++;
        }
        if (!category.isEmpty()) {
            num++;
        }
        if (stock.equals(0)) {
            num++;
        }
        return num;
    }

    public static Number attributeFilter(String name, String category, Number stock) {
        if (name != null) {
            if (category != null) {
                if (stock != null) {
                    return 1;
                }
                return 2;
            } else if (stock != null) {
                return 3;
            } else {
                return 4;
            }
        } else if (category != null) {
            if (stock != null) {
                return 5;
            } else {
                return 6;
            }
        } else if (stock != null) {
            return 7;
        } else {
            return 0;
        }
    }
}
