package com.bootdo.modular.engage.param;

import com.bootdo.config.converter.JsonListFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品库存
 *
 * @author L
 * @since 2024-02-21 13:14
 */
@Data
public class BalanceQryParam {

    @JsonListFormat
    private List<Long> shopNo;

    private Date toDate;

    private String stock;

    private String productNo;

    private String type;

    private String status;

    private String searchText;

}
