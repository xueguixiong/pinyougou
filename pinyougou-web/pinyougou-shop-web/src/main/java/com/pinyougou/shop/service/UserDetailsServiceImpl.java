package com.pinyougou.shop.service;

import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService{
    /** 注入商家服务接口代理对象 */
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("sellerService = "+ sellerService);

        //根据登陆用户名查询商家
        Seller seller = sellerService.findOne(username);
        //审核通过
        if (seller != null && "1".equals(seller.getStatus())){
            //定义List集合封装对象
            List<GrantedAuthority> authorities = new ArrayList<>();
            //添加角色
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            //返回用户
            return new User(username,seller.getPassword(),authorities);
        }
        return null;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
