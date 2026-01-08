package com.library.entityviews;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import com.library.entity.Borrowing;

public class ReturnableSubqueryProvider implements SubqueryProvider {
    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryInitiator) {
        return subqueryInitiator
            .from(Borrowing.class, "bo")
            .select("COUNT(bo.id)")
            .where("bo.book.id").eqExpression("VIEW(id)")
            .where("bo.user.id").eqExpression(":userId")
            .where("bo.status").notIn("RETURN", "RETURN_ACCEPTED")
            .end();
    }
}