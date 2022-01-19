package io.github.whitesocks.fund.funddump.service;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.github.whitesocks.fund.funddump.mapper.FundCompanyMapper;
import io.github.whitesocks.fund.funddump.model.FundCompanyBasic;
import io.github.whitesocks.fund.funddump.util.EasyMoneyFundUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FundService {

    @Autowired
    FundCompanyMapper fundCompanyMapper;




    public Boolean sync(){
        log.info("sync funds from http://fund.eastmoney.com/ start");
        Stopwatch watch = Stopwatch.createStarted();
        syncCompany();
        log.info("sync fund company end cost:{} seconds", watch.elapsed(TimeUnit.SECONDS));
        suncFundInfos();
        log.info("sync funds from http://fund.eastmoney.com/ end cost:{} seconds", watch.elapsed(TimeUnit.SECONDS));
        return true;
    }

    private void suncFundInfos() {


    }







    private void syncCompany() {
        List<String> failCode = new ArrayList<>();
        List<FundCompanyBasic> companyCodes = EasyMoneyFundUtil.getCompanyCodes();
        List<List<FundCompanyBasic>> partition = Lists.partition(companyCodes, 100);
        partition.forEach(s -> {
            List<FundCompanyBasic> collect = s.parallelStream().map(a -> {
                try {
                    FundCompanyBasic fundCompanyBasic = EasyMoneyFundUtil.parse(a.getCode());
                    fundCompanyBasic.setSuoxie(a.getSuoxie());
                    fundCompanyBasic.setLevel(a.getLevel());
                    return fundCompanyBasic;
                } catch (Exception e) {
                    failCode.add(a.getCode());
                    log.info("fund parse fail code:{}, error:{}", a.getCode(), Throwables.getStackTraceAsString(e));
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            fundCompanyMapper.creates(collect);
        });


    }


}
