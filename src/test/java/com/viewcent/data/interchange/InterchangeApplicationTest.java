package com.viewcent.data.interchange;

import com.viewcent.data.interchange.goods.GoodsService;
import com.viewcent.data.interchange.goods.WanliniuInventoryService;
import com.viewcent.data.interchange.member.WanliniuMemberService;
import com.viewcent.data.interchange.trade.TradeService;
import com.viewcent.data.interchange.utils.Response;
import com.viewcent.data.interchange.utils.ResponseStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest(classes = InterchangeApplication.class)
@RunWith(SpringRunner.class)
public class InterchangeApplicationTest
{
    
    @Autowired
    private WanliniuMemberService    wanliniuMemberService;
    
    @Autowired
    private GoodsService             goodsService;
    
    @Autowired
    private WanliniuInventoryService wanliniuInventoryService;
    
    @Autowired
    private TradeService             tradeService;
    
    /**
     * 查询会员信息
     */
    @Test
    public void executeMemeberTest()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date start_time = calendar.getTime();
        
        wanliniuMemberService.queryMemberList(null,
                null,
                null,
                start_time,
                null,
                1,
                10);
    }
    
    @Test
    public void executeMemeberDetailTest()
    {
        wanliniuMemberService.queryMemberDetails(null, null);
    }
    
    /**
     * 商品规格
     */
    @Test
    public void executeGoodsSpecificationTest()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date modify_time = calendar.getTime();
        goodsService.queryGoodsSpecification(null, null, modify_time, null, 1, 20);
    }
    
    /**
     * 商品规格list
     */
    @Test
    public void executeGoodsSpecificationListTest()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date modify_time = calendar.getTime();
        
        Response response = new Response();
        Integer page = 0;
        Integer limit = 100;
        do
        {
            response = goodsService.queryGoodswithspeclist(null, "ZXKF0048", modify_time, null, ++page, limit, "wanliniu", "json", "000000");
        }
        while (response.getStatus() == ResponseStatus.OK && (Boolean) response.getData() == true);
        
    }
    
    /**
     * 获取商品分类
     */
    @Test
    public void executeGoodsCategoryTest()
    {
        
        goodsService.queryGoodsCategoryInfo("万里牛", "json");
    }
    
    /**
     * 库存测试
     */
    @Test
    public void executeInventory()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date modify_time = calendar.getTime();
        wanliniuInventoryService.queryInventoryByModifytime("065361", modify_time, 1, 10);
    }
    
    /**
     * 交易信息
     */
    @Test
    public void executeTradeTest()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date modify_time = calendar.getTime();
        
        Response response = new Response();
        Integer page_no = 1;
        Integer page_size = 100;
        do
        {
            response = tradeService.queryTradesInfo(modify_time, false, page_no, page_size, "json", "065361", "wanliniu");
            page_no++;
        }
        while (response.getStatus() == ResponseStatus.OK && (Boolean) response.getData() == true);
        
    }
    
    @Test
    public void testCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String format = df.format(time);
        System.out.println(format);
    }
    
    /**
     * 同步万里牛门店信息
     */
    @Test
    public void shopSync()
    {
        tradeService.syncGoodsStoreCode("wanliniu", "065361");
    }
}
