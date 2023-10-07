package com.supercoding.hackathon01.repository.home;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supercoding.hackathon01.dto.home.request.ViewHomeListRequest;
import com.supercoding.hackathon01.dto.home.resposne.ViewPointListResponse;
import com.supercoding.hackathon01.entity.Home;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.supercoding.hackathon01.entity.QAddress.address1;
import static com.supercoding.hackathon01.entity.QHome.home;
import static com.supercoding.hackathon01.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class HomeCustomRepositoryImpl implements HomeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Home> findHomeList(ViewHomeListRequest viewHomeListRequest) {

        NumberExpression<Double> distance = calcDistance(viewHomeListRequest.getLatitude(), viewHomeListRequest.getLongitude());

        return jpaQueryFactory
                .select(home)
                .from(home)
                .leftJoin(home.user, user)
                .leftJoin(address1)
                .on(address1.home.id.eq(home.id))
                .where(
                        isDeletedFalse(),
                        eqCategoryId(viewHomeListRequest.getCategoryId()),
                        distance.round().intValue().lt(3000)
                )
                .fetch();
    }

    @Override
    public List<ViewPointListResponse> findPointList(ViewHomeListRequest viewHomeListRequest) {

        NumberExpression<Double> distance = calcDistance(viewHomeListRequest.getLatitude(), viewHomeListRequest.getLongitude());

        return jpaQueryFactory
                .select(Projections.constructor(ViewPointListResponse.class,
                        address1.home.id,
                        address1.latitude,
                        address1.longitude
                        ))
                .from(address1)
                .leftJoin(address1.home, home)
                .where(
                        isDeletedFalse(),
                        eqCategoryId(viewHomeListRequest.getCategoryId()),
                        distance.round().intValue().lt(3000)
                )
                .fetch();
    }

    private NumberExpression<Double> calcDistance(Double latitude, Double longitude) {
        return numberTemplate(Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                longitude, latitude, address1.longitude, address1.latitude);
    }

    private BooleanExpression isDeletedFalse() {
        return home.isDeleted.eq(false);
    }
    private BooleanExpression eqCategoryId(Long categoryId) {
        if (categoryId != null && categoryId > 0) {
            return home.category.id.eq(categoryId);
        }
        return null;
    }


}
