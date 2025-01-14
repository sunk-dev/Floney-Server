package com.floney.floney.book.repository.analyze;

import com.floney.floney.book.domain.entity.Book;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.floney.floney.book.domain.entity.QAsset.asset;
import static com.floney.floney.common.constant.Status.INACTIVE;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssetCustomRepositoryImpl implements AssetCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void inActiveAllByBook(Book book){
        jpaQueryFactory.update(asset)
            .set(asset.status,INACTIVE)
            .set(asset.updatedAt, LocalDateTime.now())
            .where(asset.book.eq(book))
            .execute();
    }
}
