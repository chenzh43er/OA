package com.shop.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shop.pojo.User;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private DefaultKaptcha captchaProducer;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/doLogout")
    public String doLogout(HttpSession session){
        session.invalidate();
        return "login";
    }

    @RequestMapping(value = "/regesiter")
    public String regesiter(User user){
        System.out.println(user);
        //不是管理员
        user.setUserisadmin(0);

        this.userService.regesiterUser(user);

        return "redirect:login";
    }

    /**
     * 跳转到注册界面
     * @return
     */
    @RequestMapping(value="/toregesiter")
    public String toregesiter()
    {
        return "regesiter";
    }

    /**
     * 用户登录
     * @param username
     * @param userPassword
     * @param valideCode
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/dologin")
    public String dologin(String username, String userPassword,
                          String valideCode, HttpSession session, Model model){
        //从session中取出验证码
        String vCode = (String) session.getAttribute("vrifyCode");

        System.out.println(vCode);

        //校验验证码
        if(!vCode.equals(valideCode)){//-->不正确
            model.addAttribute("errorMsg","验证码错误！");
            return "login";
        }

        //校验用户名密码
        List<User> list = userService.isLogin(username,userPassword);
        System.out.println(list.size());
        if(list.size() > 0 && list != null){
            User user = list.get(0);
            //保存在session
            System.out.println(user);
            session.setAttribute("user",user);
            return "redirect:/queryAllAuctions";
        }else{
            model.addAttribute("errorMsg","用户名或者密码错误！");
            return "login";
        }
    }

    /**
     * 跳转到指定页面
     * @return
     */
    @RequestMapping(value = "/login")
    public String page(){
        return "login";
    }

    /**
     * session 中注入验证码
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @RequestMapping(value = "/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();

            //httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            httpServletRequest.getSession().setAttribute("vrifyCode", "1111");

            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);

            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
