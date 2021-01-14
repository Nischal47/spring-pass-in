package com.example.base.search;

import com.example.base.search.QuerySearchCriteria;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import java.util.Optional;

class QueryUrlPredicate<T extends EntityPathBase> {
    private static final String STARTS_WITH_OPERATOR = "~";
    private static final String EQUALS_OPERATOR = ":";
    private static final String GREATER_THAN_OPERATOR = ">";
    private static final String LESS_THAN_OPERATOR = "<";
    private static final String ORDER_BY_ASCENDING = "asc";
    private static final String ORDER_BY_DESCENDING = "desc";


    private final Class<T> classType;
    private final PathMetadata metaData;

    private QueryUrlPredicate(Class parameterizedClass, EntityPathBase entityPathBase) {
        this.metaData = entityPathBase.getMetadata();
        this.classType = parameterizedClass;
    }

    static QueryUrlPredicate forClass(Class parameterizedClass, EntityPathBase entityPathBase) {
        return new QueryUrlPredicate(parameterizedClass, entityPathBase);
    }

    Optional<BooleanExpression> getPredicate(QuerySearchCriteria criteria) {

        PathBuilder<T> entityPath = new PathBuilder<>(classType, metaData);

        if (isNumeric(criteria.getValue().toString())) {
            NumberPath<Double> path = entityPath.getNumber(criteria.getKey(), Double.class);
            double value = Double.parseDouble(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case EQUALS_OPERATOR:
                    return Optional.of(path.eq(value));
                case GREATER_THAN_OPERATOR:
                    return Optional.of(path.gt(value));
                case LESS_THAN_OPERATOR:
                    return Optional.of(path.lt(value));
            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(EQUALS_OPERATOR)) {
                return Optional.of(path.eq(criteria.getValue().toString()));
            }
            if (criteria.getOperation().equalsIgnoreCase(STARTS_WITH_OPERATOR)) {
                return Optional.of(path.startsWith(criteria.getValue().toString()));
            }

        }

        return Optional.empty();
    }

    Optional<OrderSpecifier> getOrderSpecifier(QuerySearchCriteria criteria) {
        PathBuilder<T> entityPath = new PathBuilder<>(classType, metaData);
        StringPath path = entityPath.getString(criteria.getKey());
        switch (criteria.getValue().toString()) {
            case ORDER_BY_ASCENDING:
                return Optional.of(path.asc());
            case ORDER_BY_DESCENDING:
                return Optional.of(path.desc());
        }
        return Optional.empty();
    }

    private static boolean isNumeric(String s) {

        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}