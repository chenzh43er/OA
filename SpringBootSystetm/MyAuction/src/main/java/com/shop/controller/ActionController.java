package com.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
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

    /**
     * 查询所有的拍卖商品
     * @param auction
     * @param pageNum
     * @param model
     * @return
     */
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

    /**
     * 通过拍卖ID查询拍卖详情 并跳转至详情页面
     * @param model
     * @param auctionid
     * @return
     */
    @RequestMapping(value = "toAuctionDetail/{auctionid}")
    public String toAuctionDetail(Model model, @PathVariable int auctionid){

        Auction auction = this.auctionService.selectAuctionAndRecodList(auctionid);

        model.addAttribute("auctionDetail",auction);

        System.out.println(auction);

        return "auctionDetail";
    }

    /**
     * 保存竞拍记录 重定向至竞拍详情页面
     * @param record
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "saveAuctionRecord")
    public String saveAuctionRecord(Auctionrecord record, HttpSession session) throws Exception {

        User user = (User) session.getAttribute("user");

        record.setUserid(user.getUserid());

        record.setAuctiontime(new Date());

        this.auctionService.saveAuctionRecord(record);

        Integer actionid =  record.getAuctionid();
        return "redirect:toAuctionDetail/"+actionid;
    }

    /**
     * 跳转至拍卖结果界面
     * @param model
     * @return
     */
    @RequestMapping(value = "toAuctionResult")
    public String toAuctionResult(Model model){
        //查询当前正在拍卖的商品以及拍卖成功的商品

        //1.查看当前已经结束的竞拍
        List<AuctionCustomer> auctionendtime = this.auctionService.findAuctionendtime();

        //2.查看当前未结束的竞拍
        List<Auction> auctionNoendtime = this.auctionService.findAuctionNoendtime();

        model.addAttribute("endtimeList",auctionendtime);

        model.addAttribute("noendtimeList",auctionNoendtime);

        return "auctionResult";
    }
}
