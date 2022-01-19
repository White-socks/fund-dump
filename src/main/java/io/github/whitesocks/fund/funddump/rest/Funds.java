package io.github.whitesocks.fund.funddump.rest;

import io.github.whitesocks.fund.funddump.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fund")
public class Funds {


    @Autowired
    private FundService fundService;

    @GetMapping("/sync")
    public Boolean sync(){
        return fundService.sync();
    }

}
