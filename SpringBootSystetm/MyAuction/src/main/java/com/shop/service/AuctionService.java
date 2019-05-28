package com.shop.service;

import java.util.List;

import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.Auctionrecord;

public interface AuctionService {

    List<Auction> queryAllAuctions(Auction auction);

    Auction selectAuctionAndRecodList(int auctionId);

    void saveAuctionRecord(Auctionrecord record) throws Exception;

    List<AuctionCustomer> findAuctionendtime();

    List<Auction> findAuctionNoendtime();

    void addAuction(Auction auction);

}
