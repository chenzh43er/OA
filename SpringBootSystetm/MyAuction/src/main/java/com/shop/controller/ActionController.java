package com.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.pojo.Auction;
import com.shop.pojo.Auctionrecord;
import com.shop.pojo.User;
import com.shop.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ActionController {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private AuctionService auctionService;

    @RequestMapping(value = "queryAllAuctions")
    public String queryAllAuctions(@ModelAttribute("condition") Auction auction,
                                   @RequestParam(name="pageNum",required=false,defaultValue="1")int pageNum, Model model){

        PageHelper.startPage(pageNum, PAGE_SIZE);

        List<Auction> auctionList = this.auctionService.queryAllAuctions(auction);

        PageInfo page = new PageInfo(auctionList);

        System.out.println(page.toString());

        model.addAttribute("page",page);

        model.addAttribute("auctionList",auctionList);

        return "index";
    }

    @RequestMapping(value = "toAuctionDetail/{auctionid}")
    public String toAuctionDetail(Model model, @PathVariable int auctionid){

        Auction auction = this.auctionService.selectAuctionAndRecodList(auctionid);

        model.addAttribute("auctionDetail",auction);

        System.out.println(auction);

        return "auctionDetail";
    }

    @RequestMapping(value = "saveAuctionRecord")
    public String saveAuctionRecord(Auctionrecord record, HttpSession session) throws Exception {

        User user = (User) session.getAttribute("user");

        record.setUserid(user.getUserid());

        record.setAuctiontime(new Date());

        this.auctionService.saveAuctionRecord(record);

        Integer actionid =  record.getAuctionid();
        return "redirect:toAuctionDetail/"+actionid;
    }
}
