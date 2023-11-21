package com.supercoding.hackathon01.repository.home;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supercoding.hackathon01.dto.home.request.ViewHomeListRequest;
import com.supercoding.hackathon01.dto.home.request.ViewHomePageRequest;
import com.supercoding.hackathon01.dto.home.resposne.HomeList;
import com.supercoding.hackathon01.dto.home.resposne.ViewHomeListPageResponse;
import com.supercoding.hackathon01.dto.home.resposne.ViewPointListResponse;
import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.enums.HomeSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                        address1.longitude,
                        address1.home.transactionType,
                        address1.home.deposit,
                        address1.home.price
                        ))
                .from(address1)
                .leftJoin(address1.home, home)
                .where(
                        isDeletedFalse(),
                        eqCategoryId(viewHomeListRequest.getCategoryId()),
                        eqIsParking(viewHomeListRequest.getIsParking()),
                        createSquareFeetFilter(viewHomeListRequest.getSquareFeetFilter()),
                        createPriceFilter(viewHomeListRequest.getPriceFilter()),
                        distance.round().intValue().lt(2700)
                )
                .fetch();
    }

    @Override
    public ViewHomeListPageResponse findHomePageList(ViewHomePageRequest viewHomePageRequest) {

        NumberExpression<Double> distance = calcDistance(viewHomePageRequest.getLatitude(), viewHomePageRequest.getLongitude());

        HomeSortType homeSortType = HomeSortType.fromString(viewHomePageRequest.getSortType());
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        switch (homeSortType) {
            case SQUARE_DESC:
                orderSpecifiers.add(home.squareFeet.desc());
                break;
            case CREATED_DESC:
                orderSpecifiers.add(home.id.desc());
                break;
            case PRICE_ASC:
                orderSpecifiers.add(home.price.asc());
                break;
            case PRICE_DESC:
                orderSpecifiers.add(home.price.desc());
                break;
            case SQUARE_ASC:
                orderSpecifiers.add(home.squareFeet.asc());
                break;
            default:
                break;
        }

        orderSpecifiers.add(home.id.desc()); //최신 순으로 추가 정렬

        List<HomeList> result = jpaQueryFactory
                .select(Projections.constructor(HomeList.class,
                            home.id,
                            home.name,
                            home.transactionType,
                            home.deposit,
                            home.price,
                            home.squareFeet
                        ))
                .from(home)
                .leftJoin(address1)
                .on(
                        address1.home.eq(home)
                )
                .where(
                        cursorValue(viewHomePageRequest.getCursorValue(), homeSortType, viewHomePageRequest.getCursorId()),
                        isDeletedFalse(),
                        eqCategoryId(viewHomePageRequest.getCategoryId()),
                        eqIsParking(viewHomePageRequest.getIsParking()),
                        createSquareFeetFilter(viewHomePageRequest.getSquareFeetFilter()),
                        createPriceFilter(viewHomePageRequest.getPriceFilter()),
                        distance.round().intValue().lt(2700)
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .limit(10)
                .fetch();

        Long nextCursorId = calcNextCursorId(result);
        String nextCursorValue = calcNextCursorValue(result, homeSortType);

        return new ViewHomeListPageResponse(result, nextCursorId, nextCursorValue);
    }

    private Long calcNextCursorId(List<HomeList> result) {
        return result.isEmpty() ? null : result.stream()
                .mapToLong(HomeList::getHomeId)
                .min()
                .orElse(0L);
    }

    private String calcNextCursorValue(List<HomeList> result, HomeSortType shopSortType) {
        switch (shopSortType) {
            case SQUARE_DESC:
            case SQUARE_ASC:
                return result.isEmpty() ? null : String.valueOf(result.get(result.size() - 1).getSquareFeet());
            case PRICE_DESC:
            case PRICE_ASC:
                return result.isEmpty() ? null : String.valueOf(result.get(result.size() - 1).getPrice());
            case CREATED_DESC: default:
                // 기본 정렬 설정: 거리, 평균 별점이 같은 경우 최신 순으로
                return null;
        }
    }

    private BooleanExpression cursorValue(String cursorValue, HomeSortType homeSortType, Long cursorId) {
        if (cursorValue == null && cursorId == null) return null;
        switch (homeSortType) {
            case SQUARE_DESC:
                return home.squareFeet.lt(Integer.valueOf(Objects.requireNonNull(cursorValue))).or(
                        home.squareFeet.eq(Integer.valueOf(cursorValue))
                                .and(home.id.gt(cursorId))
                );
            case SQUARE_ASC:
                return home.squareFeet.gt(Integer.valueOf(Objects.requireNonNull(cursorValue))).or(
                        home.squareFeet.eq(Integer.valueOf(cursorValue))
                                .and(home.id.gt(cursorId))
                );
            case PRICE_DESC:
                return home.price.lt(Long.valueOf(Objects.requireNonNull(cursorValue))).or(
                        home.price.eq(Long.valueOf(cursorValue))
                                .and(home.id.gt(cursorId))
                );
            case PRICE_ASC:
                return home.price.gt(Long.valueOf(Objects.requireNonNull(cursorValue))).or(
                        home.price.eq(Long.valueOf(cursorValue))
                                .and(home.id.gt(cursorId))
                );
            case CREATED_DESC:
            default:
                // 기본 정렬 설정: 거리, 평균 별점이 같은 경우 최신 순으로
                return ltHomeId(cursorId);
        }
    }

    public BooleanBuilder createSquareFeetFilter(Integer value) {
        if (value == null) return null;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(home.squareFeet.isNotNull());

        switch (value) {
            case 1:
                booleanBuilder.and(home.squareFeet.lt(10));
                break;
            case 2:
                booleanBuilder.and(home.squareFeet.goe(10).and(home.squareFeet.lt(20)));
                break;
            case 3:
                booleanBuilder.and(home.squareFeet.goe(20).and(home.squareFeet.lt(30)));
                break;
            case 4:
                booleanBuilder.and(home.squareFeet.goe(30));
                break;
            default:
                break;
        }

        return booleanBuilder;
    }

    public BooleanBuilder createPriceFilter(Integer value) {
        if (value == null) return null;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(home.price.isNotNull());

        switch (value) {
            case 1:
                booleanBuilder.and(home.price.lt(500));
                break;
            case 2:
                booleanBuilder.and(home.price.goe(500).and(home.price.lt(1000)));
                break;
            case 3:
                booleanBuilder.and(home.price.goe(1000).and(home.price.lt(3000)));
                break;
            case 4:
                booleanBuilder.and(home.price.goe(3000));
                break;
            default:
                break;
        }

        return booleanBuilder;
    }

    private BooleanExpression ltHomeId(Long homeId) {
        if (homeId != null && homeId > 0) {
            return home.id.lt(homeId);
        }
        return null;
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

    private BooleanExpression eqIsParking(Boolean isParking) {
        if (isParking != null) {
            return home.isParking.eq(isParking);
        }
        return null;
    }


}
