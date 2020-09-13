
package com.viewcent.data.interchange.utils;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * @author Administrator
 */
public class DomUtil
{
    public static Document convertString2Dom(String input) throws Exception
    {
        SAXReader saxReader = new SAXReader();
        saxReader.setEncoding("utf-8");
        return saxReader.read(new InputSource(new StringReader(input)));
    }
    
    public static String json2Xml1(String json)
    {
        
        // 输入流
        json = "{\"root\":" + json + "}";
        StringReader input = new StringReader(json);
        // 输出流
        StringWriter output = new StringWriter();
        // 构建配置文件
        JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false).build();
        try
        {
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
            // 创建一个实例使用默认的缩进和换行
            writer = new PrettyXMLEventWriter(writer);
            writer.add(reader);
            reader.close();
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                output.close();
                input.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return output.toString();
    }
    
    // test
    public void jxDom(String jsonStr) throws Exception
    {
        String xmlStr = json2Xml1(jsonStr);
        Document document = convertString2Dom(xmlStr);
        String xpath = "root/data";
        List<Node> list = document.selectNodes(xpath);
        for (Node orderNode : list)
        {
            String tradeNo = getSingleNodeValue(orderNode, "reciver_money_account_list/pay_type_name");
            System.err.println("订单号：" + tradeNo);
        }
    }
    
    public static String getSingleNodeValue(Node element, String nodeName)
    {
        if (element == null)
        {
            return null;
        }
        Node node = element.selectSingleNode(nodeName);
        if (node != null)
        {
            return node.getText();
        }
        return null;
    }
    
}
