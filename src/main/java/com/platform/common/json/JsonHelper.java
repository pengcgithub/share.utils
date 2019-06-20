package com.platform.common.json;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy.PropertyNamingStrategyBase;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper
{
    private static final Logger logger = LoggerFactory
            .getLogger(JsonHelper.class);
            
    static class LowerJsonNamingStrategy extends PropertyNamingStrategyBase
    {
        
        @Override
        public String translate(String arg0)
        {
            return arg0.toLowerCase();
        }
        
    }
    
    public static String NullJsonEasyUI = "{\"total\":0,\"rows\":[]}";
    
    public static String NullObjectJson = "{}";
    
    /**
     * 将Json格式转换成对象
     * @param 目标对象Class
     * @param Json数据
     * @return
     */
    public static <T> T toObject(Class<T> cls, String strJson)
    {
        T reobj = null;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mapper.setPropertyNamingStrategy(new LowerJsonNamingStrategy());
            
            //mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); 
            //mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
                    true);
            /*
            DeserializationConfig cfg = mapper.getDeserializationConfig();
            cfg.setDateFormat(new FbsDatetimeFormat());  
            mapper = mapper.setDeserializationConfig(cfg);
            */
            reobj = mapper.readValue(strJson, cls);
            
        }
        catch (JsonParseException e)
        {
            logger.error("JsonParseException:" + e);
        }
        catch (JsonMappingException e)
        {
            logger.error("JsonMappingException:" + e);
        }
        catch (IOException e)
        {
            logger.error("IOException:" + e);
        }
        
        return reobj;
    }
    
    /**
     * 将Json格式转换成对象
     * @param 目标对象Class
     * @param Json数据
     * @return
     */
    public static <T> T toObjectByStandard(Class<T> cls, String strJson)
    {
        T reobj = null;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            //mapper.setPropertyNamingStrategy(new LowerJsonNamingStrategy());
            
            //mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); 
            //mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
                    true);
            /*
            DeserializationConfig cfg = mapper.getDeserializationConfig();
            cfg.setDateFormat(new FbsDatetimeFormat());  
            mapper = mapper.setDeserializationConfig(cfg);
            */
            reobj = mapper.readValue(strJson, cls);
            
        }
        catch (JsonParseException e)
        {
            logger.error("JsonParseException:" + e);
        }
        catch (JsonMappingException e)
        {
            logger.error("JsonMappingException:" + e);
        }
        catch (IOException e)
        {
            logger.error("IOException:" + e);
        }
        
        return reobj;
    }
    
    /**
     * 将Json格式转换成对象列表
     * @param 目标对象Class
     * @param Json数据
     * @return
     */
    public static <T> List<T> toList(Class<T> cls, String strJson)
    {
        List<T> reLst = null;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mapper.setPropertyNamingStrategy(new LowerJsonNamingStrategy());
            
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            
            JavaType jType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, cls);
            reLst = (List<T>) mapper.readValue(strJson, jType);
            
        }
        catch (JsonParseException e)
        {
            logger.error("JsonParseException:" + e);
        }
        catch (JsonMappingException e)
        {
            logger.error("JsonMappingException:" + e);
        }
        catch (IOException e)
        {
            logger.error("JsonMappingException:" + e);
        }
        return reLst;
    }
    
    /**
     * 将对象转换成Json数据
     * @param 对象
     * @return
     */
    public static String toJson(Object obj)
    {
        ObjectMapper map = new ObjectMapper();
        
        map.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        map.setPropertyNamingStrategy(new LowerJsonNamingStrategy());
        
        try
        {
            return map.writeValueAsString(obj);
        }
        catch (JsonGenerationException e)
        {
            return "{}";
        }
        catch (JsonMappingException e)
        {
            return "{}";
        }
        catch (IOException e)
        {
            return "{}";
        }
    }
    
    /**
     * 将对象转换成Json数据
     * @param 对象
     * @return
     */
    public static String toJsonByStandard(Object obj)
    {
        ObjectMapper map = new ObjectMapper();
        
        map.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        
        try
        {
            return map.writeValueAsString(obj);
        }
        catch (JsonGenerationException e)
        {
            return "{}";
        }
        catch (JsonMappingException e)
        {
            return "{}";
        }
        catch (IOException e)
        {
            return "{}";
        }
    }
    
    /**
     * 将对象列表转换成Ext格式的Json数据
     * @param 对象列表
     * @return
     */
    public static <T> String toJson(List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("totalcount:" + lst.size() + ",");
        sb.append("success:true " + ",");
        sb.append("error:\"\"" + ",");
        sb.append("data:[");
        
        if (lst.size() == 0)
        {
            sb.append("]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 将ResultSet转换成Json数据
     * @param rs
     * @return
     */
    public static String toJson(ResultSet rs)
    {
        StringBuilder sb = new StringBuilder();
        String strHead = "[";
        String strEnd = "";
        
        int index = 0;
        String strFormat = "";
        try
        {
            int colCount = 0;
            ResultSetMetaData rm = rs.getMetaData();
            
            while (rs.next())
            {
                if (index > 0)
                    sb.append(",");
                else
                {
                    int colIndex;
                    colCount = rm.getColumnCount();
                    for (colIndex = 1; colIndex <= colCount; colIndex++)
                    {
                        if (colIndex > 1)
                            strFormat += ",";
                        strFormat += "\""
                                + rm.getColumnName(colIndex).toLowerCase()
                                + "\":\"%s\"";
                    }
                }
                
                sb.append("{");
                
                sb.append(jsonRow(rs, colCount, strFormat));
                
                sb.append("}");
                
                index++;
            }
        }
        catch (SQLException e)
        {
            sb.delete(0, sb.length());
        }
        
        if (sb.length() == 0)
            sb.append("]");
        else
        {
            sb.append("]");
        }
        
        return strHead + sb.toString() + strEnd;
    }
    
    public static String toSingleJson(ResultSet rs)
    {
        StringBuilder sb = new StringBuilder();
        
        int index = 0;
        String strFormat = "";
        try
        {
            int colCount = 0;
            ResultSetMetaData rm = rs.getMetaData();
            
            if (rs.next())
            {
                if (index > 0)
                    sb.append(",");
                else
                {
                    int colIndex;
                    colCount = rm.getColumnCount();
                    for (colIndex = 1; colIndex <= colCount; colIndex++)
                    {
                        if (colIndex > 1)
                            strFormat += ",";
                        strFormat += "\""
                                + rm.getColumnName(colIndex).toLowerCase()
                                + "\":\"%s\"";
                    }
                }
                
                sb.append("{");
                sb.append(jsonRow(rs, colCount, strFormat));
                sb.append("}");
                
                index++;
            }
        }
        catch (SQLException e)
        {
            sb.delete(0, sb.length());
        }
        
        return sb.toString();
    }
    
    /**
     * 将对象列表转换成EasyUI格式的Json数据
     * @param 对象列表
     * @return
     */
    public static <T> String toJsonData(List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        if (lst == null || lst.size() == 0)
        {
            sb.append("]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        return sb.toString();
    }
    
    /**
     * 将记录集转换成Ext格式的Json数据
     * @param 记录集
     * @return
     */
    /*
    public static String toJson(ResultSet rs) {
        StringBuilder sb = new StringBuilder();
        String strHead = "{totalcount:%d,success:true,error:\"\",data:[";
        String strEnd = "}";
    
        int index = 0;
        String strFormat="";
        try {
            int colCount=0;
            while (rs.next()) {
                if (index > 0)
                    sb.append(",");
                else{
                    ResultSetMetaData rm = rs.getMetaData();
                    int colIndex;
                    colCount=rm.getColumnCount() ; 
                    for (colIndex = 1; colIndex <=  colCount; colIndex++) {
                        if (colIndex>1)
                            strFormat+=",";
                        strFormat +="\""+ rm.getColumnName(colIndex).toLowerCase() + "\":\"%s\""; 
                    }
                    //strFormat="{" + strFormat +"}";
                }
                     
                    
                sb.append("{");
                 
                sb.append(jsonRow(rs,colCount,strFormat));
                
                sb.append("}");
                
                index++;
            }
            rs.close();
        } catch (SQLException e) {
            sb.delete(0, sb.length());
        }
        finally{
            try {
                rs.close(); 
                rs.getStatement().close();
                rs.getStatement().getConnection().close();
                 
            } catch (SQLException e) { 
                e.printStackTrace();
            }
        }
        if (sb.length() == 0)
            sb.append("{}]");
        else{
            strHead=String.format(strHead, index);
            sb.append("]");
        } 
    
        return strHead + sb.toString() + strEnd;
    }
     */
    
    /**
     * 将对象列表转换成Ext格式的Json数据
     * @param 记录条数
     * @param 对象列表
     * @return
     */
    public static <T> String toJsonExt(int recordCount, List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("totalcount:" + recordCount + ",");
        sb.append("success:true " + ",");
        sb.append("error:\"\"" + ",");
        sb.append("data:[");
        
        if (lst.size() == 0)
        {
            sb.append("{}]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 将对象列表转换成EasyUI格式的Json数据
     * @param 对象列表
     * @return
     */
    public static <T> String toJsonEasyUI(List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"total\":" + lst.size() + ",");
        sb.append("\"rows\":[");
        
        if (lst.size() == 0)
        {
            sb.append("]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 将对象列表转换成EasyUI格式的Json数据
     * @param 记录条数
     * @param 对象列表
     * @return
     */
    public static <T> String toJsonEasyUI(int recordCount, List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"total\":" + recordCount + ",");
        sb.append("\"rows\":[");
        
        if (lst.size() == 0)
        {
            sb.append("]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 将对象列表转换成QUI格式的Json数据
     * @param 记录条数
     * @param 对象列表
     * @return
     */
    public static <T> String toJsonQUI(int recordCount, List<T> lst)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"pager.pageNo\":" + recordCount + ",");
        sb.append("\"pager.totalRows\":" + recordCount + ",");
        sb.append("\"rows\":[");
        
        if (lst.size() == 0)
        {
            sb.append("{}]");
        }
        else
        {
            int index = 0;
            for (T obj : lst)
            {
                if (index > 0)
                    sb.append(",");
                sb.append(toJson(obj));
                index++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 将Result转换成Ext格式的Json数据
     * @param 记录条数
     * @param 记录集
     * @return
     */
    public static String toJsonExt(int recordCount, ResultSet rs)
    {
        StringBuilder sb = new StringBuilder();
        String strHead = "{totalcount:%d,success:true,error:\"\",data:[";
        String strEnd = "}";
        
        int index = 0;
        String strFormat = "";
        try
        {
            int colCount = 0;
            ResultSetMetaData rm = rs.getMetaData();
            
            while (rs.next())
            {
                if (index > 0)
                    sb.append(",");
                else
                {
                    int colIndex;
                    colCount = rm.getColumnCount();
                    for (colIndex = 1; colIndex <= colCount; colIndex++)
                    {
                        if (colIndex > 1)
                            strFormat += ",";
                        strFormat += "\""
                                + rm.getColumnName(colIndex).toLowerCase()
                                + "\":\"%s\"";
                    }
                    //strFormat="{" + strFormat +"}";
                }
                
                sb.append("{");
                
                sb.append(jsonRow(rs, colCount, strFormat));
                
                sb.append("}");
                
                index++;
            }
            rs.close();
        }
        catch (SQLException e)
        {
            sb.delete(0, sb.length());
        }
        finally
        {
            try
            {
                rs.close();
                rs.getStatement().close();
                rs.getStatement().getConnection().close();
                
            }
            catch (SQLException e)
            {
                logger.error("SQLException:" + e);
                
            }
        }
        
        if (sb.length() == 0)
            sb.append("{}]");
        else
        {
            strHead = String.format(strHead, recordCount);
            sb.append("]");
        }
        
        return strHead + sb.toString() + strEnd;
    }
    
    /**
     * 将Result转换成EasyUI格式的Json数据 
     * @param 记录集
     * @return
     */
    public static String toJsonEasyUI(ResultSet rs)
    {
        StringBuilder sb = new StringBuilder();
        String strHead = "[";
        String strEnd = "";
        
        int index = 0;
        String strFormat = "";
        try
        {
            int colCount = 0;
            ResultSetMetaData rm = rs.getMetaData();
            
            while (rs.next())
            {
                if (index > 0)
                    sb.append(",");
                else
                {
                    int colIndex;
                    colCount = rm.getColumnCount();
                    for (colIndex = 1; colIndex <= colCount; colIndex++)
                    {
                        if (colIndex > 1)
                            strFormat += ",";
                        strFormat += "\""
                                + rm.getColumnName(colIndex).toLowerCase()
                                + "\":\"%s\"";
                    }
                }
                
                sb.append("{");
                sb.append(jsonRow(rs, colCount, strFormat));
                sb.append("}");
                
                index++;
            }
            rs.close();
        }
        catch (SQLException e)
        {
            sb.delete(0, sb.length());
        }
        finally
        {
            try
            {
                rs.close();
                rs.getStatement().close();
                rs.getStatement().getConnection().close();
                
            }
            catch (SQLException e)
            {
                logger.error("SQLException:" + e);
            }
        }
        
        if (sb.length() == 0)
            sb.append("{}]");
        else
        {
            sb.append("]");
        }
        
        return strHead + sb.toString() + strEnd;
    }
    
    /**
     * 将查询结果集转换成EasyUI格式的Json数据 
     * @param 记录条数
     * @param 查询结果集
     * @return
     */
    public static String toJsonEasyUI(int recordCount, ResultSet rs)
    {
        StringBuilder sb = new StringBuilder();
        String strHead = "{\"total\":%d,\"rows\":[";
        String strEnd = "}";
        
        int index = 0;
        String strFormat = "";
        try
        {
            int colCount = 0;
            ResultSetMetaData rm = rs.getMetaData();
            
            while (rs.next())
            {
                if (index > 0)
                    sb.append(",");
                else
                {
                    int colIndex;
                    colCount = rm.getColumnCount();
                    for (colIndex = 1; colIndex <= colCount; colIndex++)
                    {
                        if (colIndex > 1)
                            strFormat += ",";
                        strFormat += "\""
                                + rm.getColumnName(colIndex).toLowerCase()
                                + "\":\"%s\"";
                    }
                }
                
                sb.append("{");
                
                sb.append(jsonRow(rs, colCount, strFormat));
                
                sb.append("}");
                
                index++;
            }
            rs.close();
        }
        catch (SQLException e)
        {
            logger.error("SQLException:" + e);
            sb.delete(0, sb.length());
        }
        //      finally{
        //          try {
        //              rs.close(); 
        //              rs.getStatement().close();
        //              rs.getStatement().getConnection().close();
        //               
        //          } catch (SQLException e) { 
        //              e.printStackTrace();
        //          }
        //      }
        
        strHead = String.format(strHead, recordCount);
        if (sb.length() == 0)
            sb.append("]");
        else
        {
            sb.append("]");
        }
        
        return strHead + sb.toString() + strEnd;
    }
    
    private static String jsonRow(ResultSet rs, int colCount, String strFormat)
    {
        int colIndex;
        Object[] colValues = new Object[colCount];
        Object value;
        
        for (colIndex = 0; colIndex < colCount; colIndex++)
        {
            try
            {
                value = rs.getObject(colIndex + 1);
                if (value != null)
                {
                    colValues[colIndex] = string2Json(value.toString());
                    if (value instanceof Date)
                    {
                        value = rs.getTimestamp(colIndex + 1);
                        SimpleDateFormat df = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        colValues[colIndex] = df.format(value);
                    }
                    else if (value instanceof Clob)
                    {
                        Clob clob = (Clob) value;
                        colValues[colIndex] = clob.getSubString((long) 1,
                                (int) clob.length());
                    }
                }
                
                else
                    colValues[colIndex] = "";
            }
            catch (SQLException e)
            {
                colValues[colIndex] = "";
            }
        }
        return String.format(strFormat, colValues);
    }
    
    private static String string2Json(String strValue)
    {
        String newString = null;
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        java.util.regex.Matcher m = CRLF.matcher(strValue);
        if (m.find())
        {
            newString = m.replaceAll("<br>");
        }
        else
        {
            newString = strValue;
        }
        newString = newString.replaceAll("\\\"", "\\\\\"");
        newString = newString.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        
        return newString;
    }
    
    /*public static String stringtoJson(String s)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }*/
    
    public static String getColumns(ResultSet rs)
    {
        try
        {
            ResultSetMetaData rm = rs.getMetaData();
            StringBuilder strFormat = new StringBuilder();
            
            int colIndex;
            int colCount = rm.getColumnCount();
            strFormat.append("[[");
            
            for (colIndex = 1; colIndex <= colCount; colIndex++)
            {
                if (colIndex > 1)
                    strFormat.append(",");
                strFormat.append("{\"field\":\""
                        + rm.getColumnName(colIndex).toLowerCase()
                        + "\",\"title\":\"" + rm.getColumnName(colIndex)
                        + "\"}");
            }
            strFormat.append("]]");
            
            return strFormat.toString();
            
        }
        catch (SQLException e)
        {
            logger.error("SQLException:" + e);
            return "[]";
        }
    }
    
}
