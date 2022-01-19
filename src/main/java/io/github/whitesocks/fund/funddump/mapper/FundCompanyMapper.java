package io.github.whitesocks.fund.funddump.mapper;

import io.github.whitesocks.fund.funddump.model.FundCompanyBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundCompanyMapper {

    int creates(@Param("companies") List<FundCompanyBasic> companies);


}
