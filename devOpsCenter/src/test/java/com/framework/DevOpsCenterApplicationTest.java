package com.framework;


import com.framework.center.domain.company.response.CompanyVo;
import com.framework.center.domain.company.service.ICompanyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用户真实环境的集成测试
 * @author yankunw
 */

@SpringBootTest
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public class DevOpsCenterApplicationTest {

    @Autowired
    private  ICompanyService companyService;
    @Test
    public void whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
        CompanyVo result = companyService.detail(1754049390182432769L);
        Assert.assertEquals("初始智能科技有限公司", result.getName());
    }
}
