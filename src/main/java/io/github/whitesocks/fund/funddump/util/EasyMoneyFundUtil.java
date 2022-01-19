package io.github.whitesocks.fund.funddump.util;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.whitesocks.fund.funddump.model.FundCompanyBasic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.whitesocks.fund.funddump.util.EasyMoneyFundUtil.CompanyClass.BASICELEMENTCLASS;
import static io.github.whitesocks.fund.funddump.util.EasyMoneyFundUtil.CompanyClass.TTJJ_PANEL_SUB_TITLE;

public class EasyMoneyFundUtil {

    public static final String DOMAIN = "http://fund.eastmoney.com";

    interface CompanyClass{
        String BASICELEMENTCLASS = "common-basic-info";
        String TTJJ_PANEL_MAIN_TITLE = "ttjj-panel-main-title";
        String TTJJ_PANEL_SUB_TITLE = "ttjj-panel-sub-title";
        String PULL_LEFT = "pull-left";
        String PULL_RIGHT = "pull-right";
        String FUND_INFO = "fund-info";
    }




    public static List<FundCompanyBasic> getCompanyCodes(){
        try {
            Document document = Jsoup.connect(DOMAIN + "/Data/FundRankScale.aspx").get();
            String text = document.text();
            String replace = StringUtils.replace(text, "'", "\"");
            String substring = replace.substring(replace.indexOf("["), replace.lastIndexOf("]") + 1);
            return JSONArray.parseArray(substring, JSONArray.class).stream().map(s->{
                FundCompanyBasic basic = new FundCompanyBasic();
                basic.setCode(s.getString(0));
                basic.setSuoxie(s.getString(5));
                basic.setLevel(s.getString(8));
                return basic;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static FundCompanyBasic parse(String code) {
        FundCompanyBasic basic = new FundCompanyBasic();
        try {
            String url = DOMAIN + code + ".html";
            Document html = Jsoup.connect(url).get();

            Document parse = Jsoup.parse(html.getElementsByClass(BASICELEMENTCLASS).html());
            basic.setCode(code);
            basic.setName(parse.getElementsByClass(CompanyClass.TTJJ_PANEL_MAIN_TITLE).html());
            basic.setEn(parse.getElementsByClass(TTJJ_PANEL_SUB_TITLE).html());

            List<String> pullLeft = Splitter.on(" ").splitToList(parse.getElementsByClass(CompanyClass.PULL_LEFT).text());
            basic.setAddress(pullLeft.get(1));
            if (pullLeft.size() ==4){
                basic.setManager(pullLeft.get(3));
            }
            List<String> pullRight = Splitter.on(" ").splitToList(parse.getElementsByClass(CompanyClass.PULL_RIGHT).text());
            basic.setWebSide(pullRight.get(1));
            if (pullRight.size() == 4){
                basic.setPhone(pullRight.get(3));
            }
            String fundInfo = parse.getElementsByClass(CompanyClass.FUND_INFO).text();
            List<String> infos = Splitter.on(" ").splitToList(fundInfo);
            basic.setScope(infos.get(1));
            basic.setJjum(infos.get(3));
            basic.setPersonNum(infos.get(5));
            if (fundInfo.contains("暂无评级")){
                basic.setSetDate(infos.get(9));
                if (infos.size() >= 12){
                    basic.setCompanyProps(infos.get(11));
                }
            }else {
                basic.setSetDate(infos.get(13));
                if (infos.size() >= 16){
                    basic.setCompanyProps(infos.get(15));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basic;
    }



}
