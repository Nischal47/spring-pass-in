package com.example.base.search;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryUrlPredicatesBuilder {
    private final Class classType;
    private final EntityPathBase entityPathBase;

    private List<QuerySearchCriteria> params;

    private QueryUrlPredicatesBuilder(Class parameterizedClass, EntityPathBase entityPathBase) {
        params = new ArrayList<>();
        this.classType = parameterizedClass;
        this.entityPathBase = entityPathBase;

    }

    public static QueryUrlPredicatesBuilder forClass(Class parameterizedClass, EntityPathBase entityPathBase) {
        return new QueryUrlPredicatesBuilder(parameterizedClass, entityPathBase);
    }

    public QueryUrlPredicatesBuilder with(
            String key, String operation, Object value) {
        params.add(new QuerySearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression buildSearch() {
        if (params.isEmpty()) {
            return null;
        }
        List<BooleanExpression> predicates = new ArrayList<>();
        for (QuerySearchCriteria param : params) {
            Optional<BooleanExpression> exp = QueryUrlPredicate.forClass(classType, entityPathBase).getPredicate(param);
            exp.ifPresent(predicates::add);
        }

        Optional<BooleanExpression> expression = predicates.stream().reduce(BooleanExpression::and);

        return expression.get();
    }
    public List<OrderSpecifier> buildSort() {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (params.isEmpty()) {
            return orderSpecifiers;
        }
        for (QuerySearchCriteria param : params) {
            Optional<OrderSpecifier> exp = QueryUrlPredicate.forClass(classType, entityPathBase).getOrderSpecifier(param);
            exp.ifPresent(orderSpecifiers::add);
        }
        return orderSpecifiers;
    }
}
