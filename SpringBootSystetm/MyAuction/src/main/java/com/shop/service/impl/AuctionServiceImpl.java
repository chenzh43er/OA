package com.shop.service.impl;

import com.shop.mapper.AuctionCustomerMapper;
import com.shop.mapper.AuctionMapper;
import com.shop.mapper.AuctionrecordMapper;
import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.AuctionExample;
import com.shop.pojo.Auctionrecord;
import com.shop.service.AuctionService;
import com.shop.tools.CustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private AuctionCustomerMapper auctionCustomerMapper;

    @Autowired
    private AuctionrecordMapper auctionrecordMapper;

    /**
     * 根据条件查询拍卖信息
     * @param auction
     * @return
     */
    @Override
    public List<Auction> queryAllAuctions(Auction auction) {
        AuctionExample example = new AuctionExample();
        AuctionExample.Criteria criteria = example.createCriteria();

        if(auction.getAuctionname() != null){
            criteria.andAuctionnameLike("%"+auction.getAuctionname()+"%");
        }

        if(auction.getAuctiondesc() != null){
            criteria.andAuctiondescLike("%"+auction.getAuctiondesc()+"%");
        }

        if(auction.getAuctionstarttime() != null){
            criteria.andAuctionstarttimeGreaterThan(auction.getAuctionstarttime());
        }

        if(auction.getAuctionendtime() != null){
            criteria.andAuctionendtimeLessThan(auction.getAuctionendtime());
        }

        if(auction.getAuctionstartprice() != null){
            criteria.andAuctionstartpriceEqualTo(auction.getAuctionstartprice());
        }

        example.setOrderByClause("auctionstarttime desc");

        List<Auction> list = this.auctionMapper.selectByExample(example);

        return list;
    }

    @Override
    public Auction selectAuctionAndRecodList(int auctionId) {
        Auction auction = auctionCustomerMapper.selectAuctionAndRecodList(auctionId);
        return auction;
    }

    /**
     * 进行竞拍
     * 1.已经到达结束时间
     * 2.出价未超过起拍价或者当前最高出价
     * @param record
     * @throws Exception
     */
    @Override
    public void saveAuctionRecord(Auctionrecord record) throws Exception {

        Auction auction = auctionCustomerMapper.selectAuctionAndRecodList(record.getAuctionid());

        //1.判断当前点的时间是否过期
        if(auction.getAuctionendtime().after(new Date())){
            throw new CustomerException("当前拍卖已结束！！！");
        }

        //2.判断当时出价是否高于起拍价

        if(auction.getAuctionrecodList().size()>0){

            Auctionrecord max = auction.getAuctionrecodList().get(0);

            if(max.getAuctionprice().compareTo(record.getAuctionprice())>-1){
                throw new CustomerException("当前出价低于当前最高竞拍价格！！！");
            }

        }else{
            // 用你出的价格和最高价做一个比较 // BigDecimal compareTo 1 0 -1
            if(auction.getAuctionstartprice().compareTo(record.getAuctionprice())>-1){
                throw new CustomerException("当前出价低于起拍价！！！");
            }
        }

        auctionrecordMapper.insert(record);
    }

    @Override
    public List<AuctionCustomer> findAuctionendtime() {
        return null;
    }

    @Override
    public List<Auction> findAuctionNoendtime() {
        return null;
    }

    @Override
    public void addAuction(Auction auction) {

    }
}
