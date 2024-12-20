/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.jdbc.example.objects;

import io.runon.jdbc.annotation.*;

/**
 * @author macle
 */
@Table(name="T_STOCK_ITEM")
public class StockItem {
    @PrimaryKey(seq = 1)
    @Column(name = "ITEM_CD")
    private String code;
    @Column(name = "ITEM_NM")
    private String name;
    @Column(name = "ITEM_EN_NM")
    private String englishName;
    @Column(name = "MARKET_TP")
    private MarketType marketType = MarketType.KOSPI;
    @Column(name = "CATEGORY_NM")
    private String category;
    @Column(name = "WICS_NM")
    private String wics;
    @Column(name = "SUMMARY_DS")
    private String summary;

    @FlagBoolean
    @Column(name = "TRADE_FG")
    private boolean isTrade = false;


    @DateTime
    @Column(name = "LAST_UPT_DT")
    private long lastUpdateTime;


    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setWics(String wics) {
        this.wics = wics;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTrade(boolean trade) {
        isTrade = trade;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public String getCategory() {
        return category;
    }

    public String getWics() {
        return wics;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}
