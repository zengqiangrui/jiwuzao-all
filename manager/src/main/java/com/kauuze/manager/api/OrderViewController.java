package com.kauuze.manager.api;

import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.common.OrderNoPojo;
import com.jiwuzao.common.pojo.common.OrderStatusPojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.vo.order.OrderDetailVO;
import com.jiwuzao.common.vo.order.OrderSimpleVO;
import com.jiwuzao.common.vo.order.StoreOrderPagePojo;
import com.jiwuzao.common.vo.order.UserOrderPojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.OrderViewService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderViewController {

    @Resource
    private OrderViewService orderViewService;

    @RequestMapping("/getByOrderNo")
    @Cms
    public JsonResult getByOrderNo(@Valid @RequestBody OrderNoPojo pojo) {
        return JsonResult.success(orderViewService.getSimpleByOrderNo(pojo.getOrderNo()));
    }

    /**
     * 获取所有订单简单对象
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getAllSimpleOrder")
    @Cms
    public JsonResult getAllSimpleOrder(@RequestBody @Valid PagePojo pojo) {
        return JsonResult.success(orderViewService.getOrderSimple(pojo.getNum(), pojo.getSize()));
    }

    /**
     * 根据状态获取订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getSimpleOrderByStatus")
    @Cms
    public JsonResult getSimpleOrderByStatus(@RequestBody @Valid OrderStatusPojo pojo) {
        return JsonResult.success(orderViewService.getSimpleOrderByStatus(pojo.getStatus(), pojo.getPage().getNum(), pojo.getPage().getSize()));
    }

    /**
     * 根据店铺id获取订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getSimpleOrderByStoreId")
    @Cms
    public JsonResult getSimpleOrderByStore(@RequestBody @Valid StoreOrderPagePojo pojo) {
        return JsonResult.success(orderViewService.getSimpleOrderByStoreId(pojo.getStoreId(), pojo.getPage().getNum(), pojo.getPage().getSize()));
    }

    /**
     * 根据店铺名称获取订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getSimpleOrderByStoreName")
    @Cms
    public JsonResult getSimpleOrderByStoreName(@RequestBody @Valid StoreOrderPagePojo pojo) {
        return JsonResult.success(orderViewService.getSimpleOrderByStoreName(pojo.getStoreName(), pojo.getPage().getNum(), pojo.getPage().getSize()));
    }

    /**
     * 获取异常订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getSimpleExceptionOrder")
    @Cms
    public JsonResult getSimpleExceptionOrder(@RequestBody @Valid PagePojo pojo) {
        return JsonResult.success(orderViewService.getSimpleExceptionOrder(pojo.getNum(), pojo.getSize()));
    }

    @RequestMapping("/getSimpleByStoreIdStatus")
    @Cms
    public JsonResult getSimpleByStoreIdStatus(@RequestBody @Valid OrderStatusPojo pojo) {
        return JsonResult.success(orderViewService.getSimpleByStoreIdStatus(pojo.getStoreId(), pojo.getStatus(), pojo.getPage().getNum(), pojo.getPage().getSize()));
    }

    @RequestMapping("/getSimpleByStoreNameStatus")
    @Cms
    public JsonResult getSimpleByStoreNameStatus(@RequestBody @Valid OrderStatusPojo pojo) {
        return JsonResult.success(orderViewService.getSimpleByStoreNameStatus(pojo.getStoreName(), pojo.getStatus(), pojo.getPage().getNum(), pojo.getPage().getSize()));
    }

    @RequestMapping("/getSimpleByUid")
    @Cms
    public JsonResult getSimpleByUid(@RequestBody @Valid UserOrderPojo pojo) {
        PageDto<OrderSimpleVO> simpleByUid = orderViewService.getSimpleByUid(pojo.getUid(), pojo.getPage().getNum(), pojo.getPage().getSize());
        return JsonResult.success(simpleByUid);
    }

    @RequestMapping("/getSimpleByUidStatus")
    @Cms
    public JsonResult getSimpleByUidStatus(@RequestBody @Valid UserOrderPojo pojo) {
        PageDto<OrderSimpleVO> simpleByUidStatus = orderViewService.getSimpleByUidStatus(pojo.getUid(), pojo.getStatus(), pojo.getPage().getNum(), pojo.getPage().getSize());
        return JsonResult.success(simpleByUidStatus);
    }

    @RequestMapping("/getDetailOrder")
    @Cms
    public JsonResult getDetailOrder(@RequestBody @Valid OrderNoPojo pojo) {
        OrderDetailVO vo = orderViewService.getDetailOrder(pojo.getOrderNo());
        return JsonResult.success(vo);
    }


}
