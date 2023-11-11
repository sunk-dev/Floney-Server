package com.floney.floney.book.dto.process;

import com.floney.floney.book.entity.Asset;
import com.floney.floney.book.entity.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AssetInfo {
    private final float assetMoney;
    private final LocalDate date;

    // 현 시점의 자산 = 초기 자산 + 저장된 자산 데이터
    public static AssetInfo of(Asset asset, Book book) {
        return new AssetInfo(book.getAsset() + asset.getMoney(), asset.getDate());
    }

    public static AssetInfo init(float initAsset, LocalDate date) {
        return new AssetInfo(initAsset, date);
    }
}