package easy.market.repository.post;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import easy.market.request.freepost.FreePostListDto;
import easy.market.request.freepost.FreePostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static easy.market.entity.QPostComment.*;
import static easy.market.entity.QPost.*;
import static easy.market.entity.QUser.*;

@RequiredArgsConstructor
public class FreePostQueryRepositoryImpl implements FreePostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FreePostListDto> searchPostList(FreePostSearch con, Pageable pageable) {
        List<FreePostListDto> content = queryFactory
                .select(Projections.constructor(FreePostListDto.class,
                        post.id,
                        post.title,
                        user.username,
                        post.likeCount,
                        postComment.count()
                ))
                .from(post)
                .join(post.user, user)
                .leftJoin(postComment.post, post)
                .where(
                        writerNameEq(con.getCreatedBy()),
                        titleLike(con.getTitle())
                )
                .groupBy(post.id, post.title, user.username, post.likeCount)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        writerNameEq(con.getCreatedBy()),
                        titleLike(con.getTitle())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }
    public BooleanExpression writerNameEq(String createdBy) {
        if(!StringUtils.hasText(createdBy)) return null;
        return user.username.eq(createdBy);
    }

    public BooleanExpression titleLike(String title) {
        if(!StringUtils.hasText(title)) return null;
        return user.username.like("%"+title+"%");
    }
}
