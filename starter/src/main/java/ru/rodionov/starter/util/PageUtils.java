package ru.rodionov.starter.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PageUtils {
    private static final List<String> DEFAULT_SORT_ASC = List.of("createdAt,ASC");
    static String delimiter = ",";

    private PageUtils() {
    }

    public static PageRequest pageRequest(final Integer limit, final Integer offset, final Boolean descending, final String defaultSortField) {
        Sort.Direction dir = Sort.Direction.ASC;
        if (descending == null || descending)
            dir = Sort.Direction.DESC;

        return PageRequest.of(offset != null ? offset : 0, limit != null ? limit : 20,
                Sort.by(List.of(new Order(dir, defaultSortField != null ? defaultSortField : Constants.DEFAULT_SORT_FIELD))));
    }

    public static Pageable pageable(final Integer size, final Integer page, final List<String> sort) {
        var orders = retrieveSortValues(sort).stream().map(parts -> {
            String property = parts.length > 0 ? parts[0] : Constants.DEFAULT_SORT_FIELD;
            Sort.Direction direction = parts.length > 1 ? Sort.Direction.valueOf(parts[1]) : Sort.Direction.ASC;
            boolean ignoreCase = parts.length > 2 && Boolean.parseBoolean(parts[2]);
            Sort.NullHandling nullHandling = parts.length > 3 ? Sort.NullHandling.valueOf(parts[3]) : Sort.NullHandling.NATIVE;

            return new Order(direction, property, ignoreCase, nullHandling);
        }).collect(Collectors.toList());

        if (orders.isEmpty())
            orders.add(new Order(Sort.Direction.ASC, Constants.DEFAULT_SORT_FIELD));

        return PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, Sort.by(orders));
    }

    public static List<String> defaultSortAsc(final List<String> sort) {
        return sort == null || sort.isEmpty() ? DEFAULT_SORT_ASC : sort;
    }

    private static List<String[]> retrieveSortValues(final List<String> sort) {
        /*
        Метод получения списка массивов строк-значений. Логика разделена на 2 ветвления:
        - Если в запрос приходит список строк: [0]='id', [1]='DESC'.
        Так происходит, если указана сортировка по одному полю 'sort=id,DESC'.
        Далее необходимо конвертировать в список массивов строк для последующей обработки
        - Если в запрос приходит список строк с запятой: [0]='id,DESC', [1]='name,ASC' в случае запроса 'sort=id,DESC&sort=name,ASC'.
        Далее происходит конвертация через разделитель ','.
         */
        var values = new ArrayList<String[]>();

        if (sort == null || sort.isEmpty())
            return values;

        boolean needsSplit = sort.get(0).contains(delimiter);
        if (!needsSplit) {
            String property = sort.get(0);
            String direction = sort.size() > 1 ? sort.get(1) : null;
            String ignoreCase = sort.size() > 2 ? sort.get(2) : null;
            String nullHandling = sort.size() > 3 ? sort.get(3) : null;
            values.add(Stream.of(property, direction, ignoreCase, nullHandling).filter(Objects::nonNull).toArray(String[]::new));
        } else for (String s : sort) values.add(s.split(delimiter));

        return values;
    }
}
