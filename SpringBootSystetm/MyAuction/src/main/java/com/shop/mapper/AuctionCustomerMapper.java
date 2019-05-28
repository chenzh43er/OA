package com.shop.mapper;

import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.AuctionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AuctionCustomerMapper {

    Auction selectAuctionAndRecodList(int auctionId);

    List<AuctionCustomer> findAuctionendtime();

    List<Auction> findAuctionNoendtime();

}