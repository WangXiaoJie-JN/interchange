package com.viewcent.data.interchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viewcent.data.interchange.utils.CalculateUtil;
import com.viewcent.data.interchange.utils.DomUtil;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@RunWith(SpringRunner.class)
public class NormalTest
{
    @Test
    public void jsonParseTest()
    {
        String param = "{\"day\":-30,\"page_no\":0}";
        System.err.println("param" + param);
        
        JSONObject paramObejct = JSON.parseObject(param);
        Integer day = (paramObejct.getInteger("day") == null ? -1 : paramObejct.getInteger("day"));
        Integer page = (paramObejct.getInteger("page_no") == null ? 1 : paramObejct.getInteger("page_no"));
        Integer limit = (paramObejct.getInteger("page_size") == null ? 100 : paramObejct.getInteger("page_size"));
        
        System.err.println("day=" + day + "\npage=" + page + "\nlimit=" + limit);
    }
    
    @Test
    public void json2xmlTest() throws Exception
    {
        DomUtil domUtil = new DomUtil();
        String jsonStr = "{\n" + "    \"code\":0,\n" + "    \"data\":[\n" + "        {\n"
                + "            \"goods_uid\":\"28FE531209A632FEB158210E47934015\",\n" + "            \"goods_code\":\"LDJW0186\",\n"
                + "            \"goods_name\":\"天空小仔\",\n"
                + "            \"pic\":\"https://image-c.weimobwmc.com/saas-wxbiz/b3d40f717da448d3bc22daeeeabf89fa.jpg\",\n"
                + "            \"catagory_id\":\"4C9588DF5C62324D980654E55872649E\",\n" + "            \"catagory_name\":\"售卖文创\",\n"
                + "            \"tag_price\":0,\n" + "            \"unit_name\":\"件\",\n" + "            \"remark\":\"\",\n"
                + "            \"purchase_type_name\":\"无固定采购\",\n" + "            \"take_away\":0,\n" + "            \"specs\":[\n"
                + "                {\n" + "                    \"spec_uid\":\"7CCED6C536B7306AA9427D51CB8838C2\",\n"
                + "                    \"spec_code\":\"LDJW0186-GVR\",\n" + "                    \"spec_captions\":[\n"
                + "                        {\n" + "                            \"uid\":\"D1B2B1032C1A3B92AF71B97F769EE929\",\n"
                + "                            \"title\":\"。\",\n" + "                            \"value\":\"/\"\n" + "                        }\n"
                + "                    ],\n"
                + "                    \"pic\":\"https://image-c.weimobwmc.com/saas-wxbiz/b3d40f717da448d3bc22daeeeabf89fa.jpg\",\n"
                + "                    \"weight\":0,\n" + "                    \"sale_price\":1111\n" + "                },\n" + "                {\n"
                + "                    \"spec_uid\":\"7CCED6C536B7306AA9427D51CB8838C2\",\n"
                + "                    \"spec_code\":\"LDJW0186-GVR2\",\n" + "                    \"spec_captions\":[\n"
                + "                        {\n" + "                            \"uid\":\"D1B2B1032C1A3B92AF71B97F769EE929\",\n"
                + "                            \"title\":\"。\",\n" + "                            \"value\":\"/\"\n" + "                        }\n"
                + "                    ],\n"
                + "                    \"pic\":\"https://image-c.weimobwmc.com/saas-wxbiz/b3d40f717da448d3bc22daeeeabf89fa.jpg\",\n"
                + "                    \"weight\":0,\n" + "                    \"sale_price\":2222\n" + "                }\n" + "            ],\n"
                + "            \"props\":[\n" + "\n" + "            ]\n" + "        },\n" + "        {\n"
                + "            \"goods_uid\":\"9AC21A936A9637D98A5E7E665CD048CE\",\n" + "            \"goods_code\":\"LDJW0174\",\n"
                + "            \"goods_name\":\"白夜童话大狐狸摆件\",\n"
                + "            \"pic\":\"https://image-c.weimobwmc.com/saas-wxbiz/b3d40f717da448d3bc22daeeeabf89fa.jpg\",\n"
                + "            \"catagory_id\":\"4C9588DF5C62324D980654E55872649E\",\n" + "            \"catagory_name\":\"售卖文创\",\n"
                + "            \"tag_price\":0,\n" + "            \"unit_name\":\"件\",\n" + "            \"remark\":\"\",\n"
                + "            \"purchase_type_name\":\"无固定采购\",\n" + "            \"take_away\":0,\n" + "            \"specs\":[\n"
                + "                {\n" + "                    \"spec_uid\":\"270606EE113839A4A10CCBE2BE60FDF1\",\n"
                + "                    \"spec_code\":\"LDJW0174-R9T\",\n" + "                    \"spec_captions\":[\n"
                + "                        {\n" + "                            \"uid\":\"D1B2B1032C1A3B92AF71B97F769EE929\",\n"
                + "                            \"title\":\"。\",\n" + "                            \"value\":\"/\"\n" + "                        }\n"
                + "                    ],\n"
                + "                    \"pic\":\"https://image-c.weimobwmc.com/saas-wxbiz/b3d40f717da448d3bc22daeeeabf89fa.jpg\",\n"
                + "                    \"weight\":0,\n" + "                    \"sale_price\":2860\n" + "                }\n" + "            ],\n"
                + "            \"props\":[\n" + "\n" + "            ]\n" + "        }]}";
        String xmlStr = domUtil.json2Xml1(jsonStr);
        
        // 产品根目录
        String xpath = "root/data";
        Document document = DomUtil.convertString2Dom(xmlStr);
        // 遍历产品
        List<Node> list = document.selectNodes(xpath);
        
        for (Node productNode : list)
        {
            BigDecimal price = new BigDecimal(DomUtil.getSingleNodeValue(productNode, "specs/sale_price"));
            System.err.println(price);
        }
    }
    
    @Test
    public void jxDomTest() throws Exception
    {
        DomUtil domUtil = new DomUtil();
        String jsonStr = "{\n" + "    \"code\":0,\n" + "    \"data\":[\n" + "        {\n" + "            \"trade_no\":\"PT003200826001\",\n"
                + "            \"bill_type_name\":\"门店交易单\",\n" + "            \"shop_name\":\"[线下门店]大兴绿地店\",\n"
                + "            \"buyer_name\":\"散客\",\n" + "            \"total_money\":399,\n" + "            \"payment\":399,\n"
                + "            \"advance_pay\":0,\n" + "            \"post_fee\":0,\n" + "            \"discount_fee\":0,\n"
                + "            \"create_time\":1598407318000,\n" + "            \"bill_date\":1598371200000,\n"
                + "            \"pay_time\":1598407322000,\n" + "            \"buyer_remark\":\"\",\n" + "            \"changes\":0,\n"
                + "            \"debt\":0,\n" + "            \"settlement\":0,\n" + "            \"delivery_type_name\":\"自提\",\n"
                + "            \"oper_name\":\"程文君\",\n" + "            \"sale_kinds_name\":\"零售\",\n" + "            \"pos_order_list\":[\n"
                + "                {\n" + "                    \"goods_name\":\"加一元\",\n" + "                    \"goods_code\":\"￥￥￥￥￥￥\",\n"
                + "                    \"sku_code\":\"￥￥￥￥￥￥\",\n" + "                    \"price\":399,\n" + "                    \"num\":1,\n"
                + "                    \"total\":399,\n" + "                    \"discount_fee\":0\n" + "                }\n" + "            ],\n"
                + "            \"reciver_money_account_list\":[\n" + "                {\n" + "                    \"pay_type_name\":\"微信\",\n"
                + "                    \"account_name\":\"微信\",\n" + "                    \"money\":399\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"trade_no\":\"PT003200826002\",\n" + "            \"bill_type_name\":\"门店交易单\",\n"
                + "            \"shop_name\":\"[线下门店]大兴绿地店\",\n" + "            \"buyer_name\":\"散客\",\n" + "            \"total_money\":399,\n"
                + "            \"payment\":399,\n" + "            \"advance_pay\":0,\n" + "            \"post_fee\":0,\n"
                + "            \"discount_fee\":0,\n" + "            \"create_time\":1598407454000,\n" + "            \"bill_date\":1598371200000,\n"
                + "            \"pay_time\":1598407458000,\n" + "            \"buyer_remark\":\"\",\n" + "            \"changes\":0,\n"
                + "            \"debt\":0,\n" + "            \"settlement\":0,\n" + "            \"delivery_type_name\":\"自提\",\n"
                + "            \"oper_name\":\"程文君\",\n" + "            \"sale_kinds_name\":\"零售\",\n" + "            \"pos_order_list\":[\n"
                + "                {\n" + "                    \"goods_name\":\"加一元\",\n" + "                    \"goods_code\":\"￥￥￥￥￥￥\",\n"
                + "                    \"sku_code\":\"￥￥￥￥￥￥\",\n" + "                    \"price\":399,\n" + "                    \"num\":1,\n"
                + "                    \"total\":399,\n" + "                    \"discount_fee\":0\n" + "                }\n" + "            ],\n"
                + "            \"reciver_money_account_list\":[\n" + "                {\n" + "                    \"pay_type_name\":\"微信\",\n"
                + "                    \"account_name\":\"微信\",\n" + "                    \"money\":399\n" + "                }\n" + "            ]\n"
                + "        },        {\n" + "            \"trade_no\":\"PT004200826004\",\n" + "            \"bill_type_name\":\"门店交易单\",\n"
                + "            \"shop_name\":\"[线下门店]通州大稿店\",\n" + "            \"buyer_name\":\"崔豆豆\",\n" + "            \"total_money\":25.2,\n"
                + "            \"payment\":0,\n" + "            \"advance_pay\":25.2,\n" + "            \"post_fee\":0,\n"
                + "            \"discount_fee\":0,\n" + "            \"create_time\":1598409005000,\n" + "            \"bill_date\":1598371200000,\n"
                + "            \"pay_time\":1598409008000,\n" + "            \"buyer_remark\":\"\",\n" + "            \"changes\":0,\n"
                + "            \"debt\":0,\n" + "            \"settlement\":0,\n" + "            \"delivery_type_name\":\"自提\",\n"
                + "            \"oper_name\":\"褚冬雪\",\n" + "            \"sale_kinds_name\":\"零售\",\n" + "            \"pos_order_list\":[\n"
                + "                {\n" + "                    \"goods_name\":\"炸薯角\",\n" + "                    \"goods_code\":\"GDJC0002\",\n"
                + "                    \"sku_code\":\"GDJC0002-B7V\",\n" + "                    \"spec_value1\":\"/\",\n"
                + "                    \"price\":28,\n" + "                    \"num\":1,\n" + "                    \"total\":25.2,\n"
                + "                    \"discount_fee\":2.8,\n" + "                    \"unit\":\"件\"\n" + "                },\n"
                + "                {\n" + "                    \"goods_name\":\"卡布奇诺\",\n" + "                    \"goods_code\":\"ZXKF0007\",\n"
                + "                    \"sku_code\":\"ZXKF0007-EZV\",\n" + "                    \"spec_value1\":\"/\",\n"
                + "                    \"price\":30,\n" + "                    \"num\":1,\n" + "                    \"total\":0,\n"
                + "                    \"discount_fee\":30,\n" + "                    \"unit\":\"件\"\n" + "                },\n"
                + "                {\n" + "                    \"goods_name\":\"冰柠檬红茶\",\n" + "                    \"goods_code\":\"ZXKF0029\",\n"
                + "                    \"sku_code\":\"ZXKF0029-FGT\",\n" + "                    \"spec_value1\":\"/\",\n"
                + "                    \"price\":30,\n" + "                    \"num\":1,\n" + "                    \"total\":0,\n"
                + "                    \"discount_fee\":30,\n" + "                    \"unit\":\"件\"\n" + "                }\n" + "            ],\n"
                + "            \"reciver_money_account_list\":[\n" + "                {\n" + "                    \"pay_type_name\":\"储值支付\",\n"
                + "                    \"account_name\":\"现金\",\n" + "                    \"money\":0\n" + "                }\n" + "            ]\n"
                + "        }]}";
        domUtil.jxDom(jsonStr);
    }
    
    @Test
    public void acculate() throws ScriptException
    {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByExtension("js");
        double result = (Double) engine.eval("38.5+0.5*2");
        System.out.println(result);
    }
    
    @Test
    public void cal()
    {
        String str = "38.5+1*2+4/2";
        // 对表达式进行预处理，并简单验证是否是正确的表达式
        // 存放处理后的表达式
        List<String> list = new ArrayList<>();
        char[] arr = str.toCharArray();
        // 存放数字临时变量
        StringBuffer tmpStr = new StringBuffer();
        for (char c : arr)
        {
            // 如果是数字或小数点，添加到临时变量中
            if (c >= '0' && c <= '9')
            {
                tmpStr.append(c);
            }
            else if (c == '.')
            {
                if (tmpStr.indexOf(".") > 0)
                {
                    throw new RuntimeException("非法字符");
                }
                tmpStr.append(c);
            }
            // 如果是加减乘除或者括号，将数字临时变量和运算符依次放入list中
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')')
            {
                if (tmpStr.length() > 0)
                {
                    list.add(tmpStr.toString());
                    tmpStr.setLength(0);
                }
                list.add(c + "");
            }
            // 如果是空格，跳过
            else if (c == ' ')
            {
                continue;
            }
            else
            {
                throw new RuntimeException("非法字符");
            }
        }
        if (tmpStr.length() > 0)
        {
            list.add(tmpStr.toString());
        }
        // 初始化后缀表达式
        List<String> strList = new ArrayList<>();
        // 运算过程中，使用了两次栈结构，第一次是将中缀表达式转换为后缀表达式，第二次是计算后缀表达式的值
        Stack<String> stack = new Stack<>();
        // 声明临时变量，存放出栈元素
        String tmp;
        // 1. 将中缀表达式转换为后缀表达式
        for (String s : list)
        {
            // 如果是左括号直接入栈
            if (s.equals("("))
            {
                stack.push(s);
            }
            // 如果是右括号，执行出栈操作，依次添加到后缀表达式中，直到出栈元素为左括号，左括号和右括号都不添加到后缀表达式中
            else if (s.equals(")"))
            {
                while (!(tmp = stack.pop()).equals("("))
                {
                    strList.add(tmp);
                }
            }
            // 如果是加减乘除，弹出所有优先级大于或者等于该运算符的栈顶元素（栈中肯定没有右括号，认为左括号的优先级最低），然后将该运算符入栈
            else if (s.equals("*") || s.equals("/"))
            {
                while (!stack.isEmpty())
                {
                    // 取出栈顶元素
                    tmp = stack.peek();
                    if (tmp.equals("*") || tmp.equals("/"))
                    {
                        stack.pop();
                        strList.add(tmp);
                    }
                    else
                    {
                        break;
                    }
                }
                stack.push(s);
            }
            else if (s.equals("+") || s.equals("-"))
            {
                while (!stack.isEmpty())
                {
                    // 取出栈顶元素
                    tmp = stack.peek();
                    if (!tmp.equals("("))
                    {
                        stack.pop();
                        strList.add(tmp);
                    }
                    else
                    {
                        break;
                    }
                }
                stack.push(s);
            }
            // 如果是数字，直接添加到后缀表达式中
            else
            {
                strList.add(s);
            }
        }
        // 最后依次出栈，放入后缀表达式中
        while (!stack.isEmpty())
        {
            strList.add(stack.pop());
        }
        // 2.计算后缀表达式的值
        Stack<BigDecimal> newStack = new Stack<>();
        for (String s : strList)
        {
            // 若遇运算符，则从栈中退出两个元素，先退出的放到运算符的右边，后退出的放到运算符左边，
            // 运算后的结果再进栈，直到后缀表达式遍历完毕
            if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
            {
                BigDecimal b1 = newStack.pop();
                BigDecimal b2 = newStack.pop();
                switch (s)
                {
                    case "+":
                        newStack.push(b2.add(b1));
                        break;
                    case "-":
                        newStack.push(b2.subtract(b1));
                        break;
                    case "*":
                        newStack.push(b2.multiply(b1));
                        break;
                    case "/":
                        newStack.push(b2.divide(b1, 9, BigDecimal.ROUND_HALF_UP));
                        break;
                }
            }
            // 如果是数字，入栈
            else
            {
                newStack.push(new BigDecimal(s));
            }
        }
        // 最后，栈中仅有一个元素，就是计算结果
        System.err.println(newStack.peek().toString());
    }
    
    @Test
    public void acculateRealValue() throws ScriptException
    {
        BigDecimal value = null;
        String column = "1+2*(3+1)/1-3";
        String[] col = column.split("[\\+\\-\\*\\/\\(\\)]");
        value = CalculateUtil.calculate(column);
        
        System.err.println("==============1+2*(3+1)/1-3=" + value);
        
    }
    
    @Test
    public void fileExitsTest()
    {
        String path = "src/main/resources";
        File file = new File(path);
        if (file.isDirectory())
        {
            System.err.println("存在文件夹");
        }
        else
        {
            System.err.println("不存在文件夹");
        }
    }
    
    @Test
    public void nameTest()
    {
        String pwd = "21232f297a57a5a743894a0e4a801fc3";
        
        String resourcePwd = "admin";
        
    }
}
