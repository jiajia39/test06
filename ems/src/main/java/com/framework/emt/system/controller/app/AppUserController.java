package com.framework.emt.system.controller.app;

import cn.hutool.core.util.ObjectUtil;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.admin.system.enums.NotNullCheckCode;
import com.framework.admin.system.vo.UserVO;
import com.framework.admin.system.wrapper.UserWrapper;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 我的 移动端控制层
 *
 * @author gaojia
 * @since 2023-08-20
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"移动端-我的"})
@ApiSupport(order = 25)
public class AppUserController extends BaseController {

    private final IUserDao userService;

    @GetMapping("/app/user/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取当前用户详情")
    public R<UserVO> detail() {
        User detail = (User) this.userService.getById(AuthUtil.getUserId());
        if (ObjectUtil.isEmpty(detail)) {
            throw new ServiceException(NotNullCheckCode.NOT_FOUND_DATA_FILTERING);
        } else {
            return R.data(UserWrapper.build().entityVO(detail));
        }
    }

}
