package easy.market.repository.post;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import easy.market.request.freepost.FreePostListDto;
import easy.market.request.freepost.FreePostSearch;
import easy.market.request.freepost.SortBy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static easy.market.entity.QPostComment.*;
import static easy.market.entity.QPost.*;
import static easy.market.entity.QUser.*;

@Slf4j
@RequiredArgsConstructor
public class FreePostQueryRepositoryImpl implements FreePostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FreePostListDto> searchPostList(FreePostSearch con, Pageable pageable) {
        log.info("search={}", con);
        log.info("pageable={}", pageable);

        List<FreePostListDto> content = queryFactory
                .select(Projections.constructor(FreePostListDto.class,
                        post.id,
                        post.title,
                        user.username,
                        post.likeCount,
                        post.viewCount,
                        postComment.count(),
                        post.createdDate
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(postComment).on(postComment.post.eq(post))
                .where(
                        writerNameEq(con.getCreatedBy()),
                        titleLike(con.getTitle())
                )
                .groupBy(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderBy(con.getSortBy()))
                .fetch();

        log.info("content={}", content);

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        writerNameEq(con.getCreatedBy()),
                        titleLike(con.getTitle())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?> getOrderBy(SortBy sortBy) {
        if(sortBy == null) return post.createdDate.desc();
        switch (sortBy) {
            case CREATED_DATE_ASC:
                return post.createdDate.asc();
            case CREATED_DATE_DESC:
                return post.createdDate.desc();
            case LIKE_COUNT_ASC:
                return post.likeCount.asc();
            case LIKE_COUNT_DESC:
                return post.likeCount.desc();
            case VIEW_COUNT_ASC:
                return post.viewCount.asc();
            case VIEW_COUNT_DESC:
                return post.viewCount.desc();
            default:
                return post.createdDate.desc();
        }
    }

    private BooleanExpression writerNameEq(String createdBy) {
        if(!StringUtils.hasText(createdBy)) return null;
        return user.username.eq(createdBy);
    }

    private BooleanExpression titleLike(String title) {
        if(!StringUtils.hasText(title)) return null;
        return post.title.like("%"+title+"%");
    }
}
