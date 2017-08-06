package com.share.string;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 * 字符串工具类
 * @author pengc
 * @see com.share
 * @since 2017/08/06
 */
public class StringUtil extends org.apache.commons.lang.StringUtils  {

	public static final String SPLIT_COMMA_STRING = "#%2C#";
	public static final String SPLIT_COMMA = ",";
	public static final String SPLIT_VERTICAL_STRING = "#%7C#";
	public static final String SPLIT_VERTICAL = "|";
	public static final String SPLIT_RETURNANDCHANGEROW = "\r\n";
	public static final String HTML_QUOT_STRING = "&quot;";
	public static final String HTML_QUOT = "\"";
	public static final String HTML_AMP_STRING = "&amp;";
	public static final String HTML_AMP = "&";
	public static final String HTML_LT_STRING = "&lt;";
	public static final String HTML_LT = "<";
	public static final String HTML_GT_STRING = "&gt;";
	public static final String HTML_GT = ">";
	public static final String STRING_BLANK = " ";

	/**
	 * 字符串是否为空<br/>
	 * 
	 * @param str 字符串
	 * @return 如果为空发挥true，否则返回false
	 */
	public static boolean isNull(Object str) {
		return (str == null || str.toString().trim().length() <= 0);
	}

	/**
	 * 字符串是否为空
	 * 
	 * @param str 字符串
	 * @return 返回去空格的字符串
	 */
	public static String trim(String str) {
		return StringUtil.isNotNull(str) ? str.trim() : "";
	}

	/**
	 * 字符串是否为空
	 * 
	 * @param str 字符串
	 * @return 不为空返回true，否则返回false
	 */
	public static boolean isNotNull(Object str) {
		return (str != null && str.toString().trim().length() > 0);
	}

	@SuppressWarnings("unchecked")
	public static String filterStringForCell(String input) {
		if (isNull(input)) return "";
		return input.replaceAll(SPLIT_RETURNANDCHANGEROW, " ").replaceAll(
				HTML_QUOT, "“");
	}

	/**
	 * Object to String
	 *
	 * @param obj 转化变量
	 * @return 返回string字符串，如果为空则返回null
	 */
	public static String getStr(Object obj) {
		return obj != null ? String.valueOf(obj) : "";
	}

	/**
	 * 使用Map中的value替换Input中的Key
	 * 
	 * @param input
	 * @param m
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String filter(String input, Map m) {
		if (isNull(input)) return "";
		if (m == null)
			return input.replaceAll(SPLIT_COMMA_STRING, SPLIT_COMMA)
					.replaceAll(SPLIT_VERTICAL_STRING, SPLIT_VERTICAL);
		Set st = m.keySet();
		Iterator<String> iterator = st.iterator();
		while (iterator.hasNext()) {
			String mk = iterator.next();
			input = input.replaceAll(mk, (String) m.get(mk));
		}
		return input;
	}

	public static String filterHtml(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}
		}
		return filtered.toString();
	}
	/**
	 * 判断两个字符是否相等
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null)
			return false;
		return (s1.compareTo(s2) == 0) ? true : false;
	}

	/**
	 * 字符串是否全是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		if (str != null) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			return isNum.matches();
		} else {
			return false;
		}

	}

	public static boolean isNum(char ch) {
		return Character.isDigit(ch);
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 返回true,Ascill字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 字符串是否以数字结尾
	 * 
	 * @param str
	 * @return
	 */
	public static boolean endWithNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+$");
		Matcher haveNum = pattern.matcher(str);
		return haveNum.find();
	}

	/**
	 * 将字符串中的表达式分割出来(表达式分隔符不能嵌套)
	 * 
	 * @param str
	 *            字符串
	 * @param startFlag
	 *            开始标记
	 * @param endFlag
	 *            结束标记
	 * @return 表达式列
	 */
	public static Set<String> splitBySBrackets(String str, String startFlag,
			String endFlag) {
		str = str.trim();
		Set<String> names = new TreeSet<String>();
		int sj = str.indexOf(startFlag);
		int sk = str.indexOf(endFlag, sj);
		while ((sj > -1) && (sk > sj)) {
			String tn = str.substring(sj, ++sk);
			names.add(tn);
			str = str.substring(sk);
			sj = str.indexOf(startFlag);
			sk = str.indexOf(endFlag, sj);
		}
		return names;
	}

	/**
	 * 获得首字母
	 * 
	 * @param s
	 * @return
	 */
	public static String getInitial(String s) {
		return s.substring(0, 1);
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return 得到的字符串长度
	 */
	public static int Len(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
		// return s.getBytes().length;
	}

	/**
	 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @param origin
	 *            原始字符串
	 * @param len
	 *            截取长度(一个汉字长度按2算的)
	 * @param c
	 *            后缀
	 * @return 返回的字符串
	 */
	public static String substring(String origin, int len, String c) {
		if (origin == null || origin.equals("") || len < 1)
			return "";
		byte[] strByte = new byte[len];
		if (len > Len(origin)) {
			return origin + c;
		}
		try {
			System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = (int) strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if (count % 2 != 0) {
				len = (len == 1) ? ++len : --len;
			}
			return new String(strByte, 0, len, "GBK") + c;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static int f(int x) {
		int c = 0;
		while (x != 0) {
			System.out.print(x + "||" + (x - 1) + " is");
			System.out.print(x + "->");
			x = x & (x - 1);
			System.out.println(x);
			c++;
		}
		return c;
	}

	public static long get() {
		long c = 0;
		char a = 0x48;
		char b = 0x52;

		c = b << 8 | a;
		return c;
	}

	/**
	 * 替换字符
	 * 
	 * @param parentStr
	 * @param ch
	 *            被替换字符
	 * @param rep
	 *            代替字符
	 * @return
	 */
	public static String replaceStr(String parentStr, String ch, String rep) {
		int i = parentStr.indexOf(ch);
		StringBuffer sb = new StringBuffer();
		if (i == -1)
			return parentStr;
		sb.append(parentStr.substring(0, i) + rep);
		if (i + ch.length() < parentStr.length())
			sb.append(replaceStr(parentStr.substring(i + ch.length(), parentStr
					.length()), ch, rep));
		return sb.toString();
	}

	/**
	 * 
	 * @param arg
	 *            格式：176839703545251841,0|176839703545251841,0|176839703545251841
	 *            ,0
	 * @return
	 */
	public static List<Long> parseString(String arg) {
		List<Long> resultList = new ArrayList<Long>();
		String regex = "(([123456789]\\d*),[01])(\\|[123456789]\\d*,[01])*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(arg);
		if (m.matches()) {
			boolean b = m.find(0);
			while (b) {
				int nStart = m.end(1);
				long v = Long.valueOf(m.group(2));
				resultList.add(v);
				b = m.find(nStart);
			}
		}
		return resultList;
	}

	/**
	 * 
	 * 过滤掉字符窜中的“”和‘
	 */
	public static String tripChar(String resource) {
		if (null == resource)
			return resource;
		if (resource.indexOf("'") >= 0) {
			resource = resource.replace("'", "");
		}
		if (resource.indexOf("\"") >= 0) {
			resource = resource.replace("\"", "");
		}
		return resource;
	}

	/**
	 * 
	 * 整数数组转字符串数组
	 */
	public static String[] swtichIntToString(Integer[] arr, String per) {
		int len = arr.length;
		String[] s = new String[len];
		for (int i = 0; i < len; i++) {
			s[i] = per + arr[i];
		}
		return s;
	}

	/**
	 * 是否为经纬度
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDegree(String str) {
		if (str != null) {
			String reg = "(\\d{0,3}\\.\\d{1,6})||(\\d{0,3})||(0\\.\\d{1,6})";
			return str.matches(reg);
		} else {
			return false;
		}
		// str.matches(reg);

	}

	/**
	 * 获取查询条件
	 * 
	 * @return
	 */
	public static String getConditionByArr(StringBuffer sql, int[] o) {

		if (o != null) {
			int len = o.length;
			// StringBuffer sb = new StringBuffer();
			if (len == 1) {
				return sql.append("=?").toString();
			} else if (len > 1) {
				sql.append("in (");
				for (int i = 0; i < len; i++) {
					sql.append("?");
					if (i < len - 1) {
						sql.append(",");
					}
				}
				sql.append(")");
				return sql.toString();
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 检验是否为电话
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		String reg = "^\\d{3,4}(\\-)?[1-9]\\d{7,8}$";
		if (!isNull(phone)) {
			return phone.trim().matches(reg);
		}
		return false;

	}

	/**
	 * 检验是否为移动电话
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		String reg = "^(\\+\\d{2,3}\\-)?\\d{11}$";
		if (!isNull(mobile)) {
			return mobile.trim().matches(reg);
		}
		return false;

	}

	public static boolean isMail(String mail) {
		String reg = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
		if (!isNull(mail)) {
			return mail.trim().matches(reg);
		}
		return false;
	}

	/**
	 * 是否数字
	 * 
	 * @param num
	 *            整数值
	 * @param max
	 *            开始
	 * @param min 结束
	 * @return
	 */
	public static boolean isNumber(String num, int max, int min) {
		String reg = "^((-)?\\d{" + max + "," + min + "})$";
		if (isNotNull(num)) {
			return num.trim().matches(reg);
		}
		return false;
	}
	/**
	 * 是否数字
	 * 
	 * @param num
	 *            整数值
	 * @param max
	 *            开始
	 * @param min 结束
	 * @return
	 */
	public static boolean isZs(String num, int max, int min) {
		String reg = "^(\\d{" + max + "," + min + "})$";
		if (isNotNull(num)) {
			return num.trim().matches(reg);
		}
		return false;
	}
	/**
	 * 是否小数
	 * 
	 * @param num
	 *            整数值
	 * @param b
	 *            整数位
	 * @param e
	 *            小数位
	 * @return
	 */
	public static boolean isNum(String num, int b, int e) {
		String reg = "^((\\d{1," + b + "}\\.\\d{1," + e + "})|(\\d{1," + b
				+ "})|(0\\.\\d{1," + e + "}))$";
		if (isNotNull(num)) {
			return num.trim().matches(reg);
		}
		return false;
	}

	/**
	 * 是否地址
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isURL(String url){
	 if(isNotNull(url)){
         return false;
     }
     String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
         + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
         + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
         + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
         + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
         + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
         + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
         + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
//     Pattern p = Pattern.compile(regEx);
//     Matcher matcher = p.matcher(url); 
     return url.trim().matches(regEx);
}
	/**
	 * 是否为一定长度的英文和数字的组合
	 * @param str 检验的字符串
	 * @param min 开始
	 * @param max 结束
	 * @return
	 */
	public static boolean  isNW(String str ,int min, int max) {
		String reg="^[A-Za-z0-9]{" + min + "," + max + "}$";
		if (isNotNull(str)) {
			System.out.println(str.trim().matches(reg));
			return str.trim().matches(reg);
		}
		return false;
		
	}
	/**
	 * 
	 *<p>检验字符串是否以id分隔的</p>
	 * @return
	 * @author liujian.zhang
	 * @date 2013-12-3下午12:29:50
	 */
	public static boolean isIds(String ids){
		String reg="[0-9]{1,11}(,[0-9]{1,11})*";
		
		if (isNotNull(ids)) {
			return ids.trim().matches(reg);
		}
		return false;
	}
	/**
	 * 
	 * <p></p>
	 * @param f
	 * @return
	 * @author 张柳健
	 * @date 2014-6-26下午7:50:04
	 */
	public static Float parseFloat(String f){
	
		
		if (isNotNull(f)&&isNum(f,11,2)) {
			return Float.parseFloat(f);
		}
		return null;
	}
	/**
	 * 
	 * <p></p>
	 * @param f
	 * @return
	 * @author 张柳健
	 * @date 2014-6-26下午7:50:04
	 */
	public static Integer parseInt(String f){
	
		
		if (isNotNull(f)&&isNumber(f,1,11)) {
			return Integer.parseInt(f);
		}
		return null;
	}
	/**
	 * 
	 * <p></p>
	 * @param f
	 * @return
	 * @author 张柳健
	 * @date 2014-6-26下午7:50:04
	 */
	public static Long parseLong(String f){
	
		
		if (isNotNull(f)&&isNumber(f,1,20)) {
			return Long.parseLong(f);
		}
		return null;
	}
	/**
	 * 
	 * <p></p>
	 * @param f
	 * @return
	 * @author 张柳健
	 * @date 2014-6-26下午7:50:04
	 */
	public static Short parseShort(String f){
	
		
		if (isNotNull(f)&&isNumber(f,1,4)) {
			return Short.parseShort(f);
		}
		return null;
	}
	/**
	 * 
	 * <p></p>
	 * @param f
	 * @return
	 * @author 张柳健
	 * @date 2014-6-26下午7:50:04
	 */
	public static Byte parseByte(String f){
	
		
		if (isNotNull(f)&&isNum(f,2,0)) {
			return Byte.parseByte(f);
		}
		return null;
	}
	
	public static String StringToZw(String str){
		String utf=null;
		if(isNotNull(str)){
			 try {
				 utf= new String(str.getBytes("iso8859-1"),"gb2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} 
		 return utf;
	}
	/**
	 * 
	 * <p></p>
	 * @param s
	 * @return
	 * @author 张柳健
	 * @date 2014-9-17下午2:35:07
	 */
	public static boolean isUpper(String s){
		if(s.matches("[A-Z]")){
			return true;
		}
		return false;
	}
	  /**
     * 基本功能：替换标记正常显示
     *
     * @param input
     * @return String
     */
    public static String replaceTag(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuilder filtered = new StringBuilder(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '\'':
                    filtered.append("&#39;");
                    break;
                case '|':
                    filtered.append("&#124;");
                    break;
                case '(':
                    filtered.append("&#40;");
                    break;
                case ')':
                    filtered.append("&#41;");
                    break;
                case '\\':
                    filtered.append("&#92;");
                    break;
                /*case '&':
                    filtered.append("&amp;");
                    break;*/
//                case '/':
//                    filtered.append("&frasl;");
//                    break;
                default:
                    filtered.append(c);
            }

        }
        return (filtered.toString());
    }

    /**
     * 过滤html标记返回文本内容
     *
     * @param htmlStr
     * @return
     */
    public static String getClearTagString(String htmlStr) {
        String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
        Pattern pattern = Pattern.compile(regxpForHtml);

        Matcher matcher = pattern.matcher(htmlStr);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 判断标记是否存在
     *
     * @param input
     * @return boolean
     */
    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if (isNotBlank(input)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                    case '\'':
                        flag = true;
                        break;
                    case '|':
                        flag = true;
                        break;
                    case '(':
                        flag = true;
                        break;
                    case ')':
                        flag = true;
                        break;
                    case '\\':
                        flag = true;
                        break;
                }
                if (flag) {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;

    }

    /**
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @param strings
     * @param str
     * @return
     */
    public static boolean contains(String[] strings, String str) {
        if (strings == null || strings.length == 0) {
            return false;
        }

        if (str == null) {
            return false;
        }

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(str)) {
                return true;
            }
        }

        return false;
    }

    //字节转KB
    public static String convertByteToKB(long size, String formatPattern, boolean addPostfix) {
        if (size <= 0) {
            return "0KB";
        }

        if (size < 1000) {
            return "1KB";
        }

        if (StringUtil.isEmpty(formatPattern)) {
            formatPattern = "####.000000";
        }

        DecimalFormat df1 = new DecimalFormat(formatPattern);
        String r = df1.format(size * 0.1 / (1024 * 0.1));

        return StringUtil.substring(r, 0, StringUtil.indexOf(r, '.') + 3) + (addPostfix ? " KB" : "");
    }

    //字节转MB
    public static String convertByteToMB(long size, String formatPattern, boolean addPostfix) {
        if (StringUtil.isEmpty(formatPattern)) {
            formatPattern = "####.000000";
        }

        DecimalFormat df1 = new DecimalFormat(formatPattern);

        // return df1.format(size * 0.1 / (1024 * 1024 * 0.1)) + (addPostfix ? " MB" : "");
        String r = df1.format(size * 0.1 / (1024 * 1024 * 0.1));

        if (".".equals(StringUtil.substring(r, 0, 1))) {
            r = "0" + r;
        }
        return StringUtil.substring(r, 0, StringUtil.indexOf(r, '.') + 3) + (addPostfix ? " MB" : "");
    }

    /**
     * @param size
     * @param formatPattern
     * @param addPostfix
     * @return
     */
    public static String convertByteToGB(long size, String formatPattern, boolean addPostfix) {
        if (StringUtil.isEmpty(formatPattern)) {
            formatPattern = "####.000000";
        }

        DecimalFormat df1 = new DecimalFormat(formatPattern);

        /// return df1.format(size * 0.1 / (1024 * 1024 * 1024 * 0.1)) + (addPostfix ? " GB" : "");

        String r = df1.format(size * 0.1 / (1024 * 1024 * 1024 * 0.1));

        return StringUtil.substring(r, 0, StringUtil.indexOf(r, '.') + 3) + (addPostfix ? " GB" : "");
    }

    /**
     * @param size
     * @param formatPattern
     * @param addPostfix
     * @return
     */
    public static String convertByteToSuitableSize(long size, String formatPattern, boolean addPostfix) {

        if (size > FileUtils.ONE_GB) {
            return convertByteToGB(size, formatPattern, addPostfix);
        } else if (size > FileUtils.ONE_MB) {
            return convertByteToMB(size, formatPattern, addPostfix);
        } else {
            return convertByteToKB(size, formatPattern, addPostfix);
        }
    }

    /**
     * @param size
     * @param formatPattern
     * @param addPostfix
     * @param unit
     * @return
     */
    public static String convertByteToSuitableSize(long size, String formatPattern, boolean addPostfix, String unit) {

        if (size > FileUtils.ONE_GB) {
            return convertByteToGB(size, formatPattern, addPostfix);
        } else if (size > FileUtils.ONE_MB) {
            if ("gb".equals(unit)) {
                return convertByteToGB(size, formatPattern, addPostfix);
            } else {
                return convertByteToMB(size, formatPattern, addPostfix);
            }
        } else {
            if ("gb".equals(unit)) {
                return convertByteToGB(size, formatPattern, addPostfix);
            } else if ("mb".equals(unit)) {
                return convertByteToMB(size, formatPattern, addPostfix);
            } else {
                return convertByteToKB(size, formatPattern, addPostfix);
            }

        }
    }

    /**
     * @param e
     * @return
     */
    public static String encodeBase64(String e) {
        String result = null;

        if (isNotBlank(e)) {
            result = new String(Base64.encodeBase64(e.getBytes()));
        }

        return result;
    }

    /**
     * @param e
     * @return
     */
    public static String decodeBase64(String e) {
        String result = null;

        if (isNotBlank(e)) {
            result = new String(Base64.decodeBase64(e.getBytes()));
        }

        return result;
    }

    /**
     * 将list中元素转化成字符串类型。list中值为null的元素将被删除
     *
     * @param list 要被转化的集合
     * @return 包含字符串的集合
     */
    public static List toStringList(List list) {
        List result = new ArrayList(list.size());
        Object obj;
        for (int i = 0, n = list.size(); i < n; i++) {
            obj = list.get(i);
            if (obj != null) {
                result.add(obj.toString());
            }
        }
        return result;
    }

    /**
     * 将分号等分割符统一成“|”分隔符。如：“;;aa1;;aa;;qq”处理成“aa1|aa|qq|”
     *
     * @param raw              原始字符串
     * @param separators       分割字符
     * @param uniformSeparator 使用统一的分割符
     * @return 处理后的字符串
     */
    public static String uniformSeparator(String raw, String separators, String uniformSeparator) {
        String agsToCheck = raw;
        StringBuffer sb = new StringBuffer(raw.length());
        sb.append(uniformSeparator);
        int repeat = 0;
        char testWord;
        for (int i = 0; i < agsToCheck.length(); i++) {
            testWord = agsToCheck.charAt(i);
            if (separators.indexOf(testWord) != -1) {
                if (i == 0) {
                    agsToCheck = agsToCheck.replaceFirst("\\" + testWord, StringUtil.EMPTY);
                    i--;
                } else {
                    if (repeat == 0) {
                        sb.append(uniformSeparator);
                    }
                    repeat += 1;
                }
            } else {
                if (repeat > 0) {
                    repeat = 0;
                }
                sb.append(testWord);
            }
        }

        if (sb.lastIndexOf(uniformSeparator) != sb.length() - 1) {
            sb.append(uniformSeparator);
        }
        return sb.toString();
    }

    /**
     * 将一个Long类型转换成一个指定长度的字符串，不够前补0
     *
     * @param tempLong
     * @param length   最小宽度 转换之后最少几位
     * @return
     */
    private static String autoComplete(Long tempLong, int length) {
        /**
         * 处理空指针问题
         */
        if (tempLong == null) {
            tempLong = Long.valueOf(0);
        }

        /**
         * 代表前面补充0
         * 代表长度为4
         * d代表参数为十进制
         */
        final String regString = "%0" + String.valueOf(length) + "d";

        return String.format(regString, tempLong);
    }

    /**
     * 将带有中文的url链接加密
     *
     * @param url     链接地址
     * @param charset 字符集
     * @return 加密过的url
     */
    public static String encodeUrlWithCharacters(String url, String charset) {
        try {
            Matcher matcher = Pattern.compile("[^\\x00-\\xff]").matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp, charset));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 提取出content中img内容.
     *
     * @return 提取的图片
     */
    public static List<String> getFileUrls(String content) {
        if (isNotBlank(content)) {
            List<String> resultList = new ArrayList<String>();
            String regxp = "(src|href)=\"([^\"]+)\"";

            Pattern p = Pattern.compile(regxp);
            Matcher m = p.matcher(content);

            while (m.find()) {
                if (m.group(2).indexOf("fckeditor") == -1)
                    resultList.add(m.group(2));//获取被匹配的部分,并且去除fck的表情图片.
            }
            return resultList;
        }
        return Collections.emptyList();
    }


    /**
     * 过滤指定标签.
     *
     * @param str 待过滤的字符串
     * @param tag 过滤的标签
     * @return 过滤后的字符串
     */
    private String filterHtmlTag(String str, String tag) {
        if (isNotBlank(str)) {
            String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
            Pattern pattern = Pattern.compile(regxp);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            boolean result1 = matcher.find();

            while (result1) {
                matcher.appendReplacement(sb, "");
                result1 = matcher.find();
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
        return str;
    }

    /**
     * @param tempStr
     * @return
     */
    public static String capitalizeStr(String tempStr) {
        if (StringUtil.isBlank(tempStr)) {
            return StringUtil.EMPTY;
        }

        StringBuffer stringBuffer = new StringBuffer(400);

        String[] arrStr = StringUtil.split(tempStr, "_");
        for (int i = 0; i < arrStr.length; i++) {
            stringBuffer.append(StringUtil.capitalize(arrStr[i]));
        }

        return stringBuffer.toString();
    }

    /**
	 * 移除特殊字符
     * @param shortPinyin 字符串变量
     * @return 返回被移除的字符串变量
     */
    public static String replaceSpecialCharacters(String shortPinyin) {
        String regEx = "[-`~!@#$%^&*()+=|{}':;',\\[\\]\".<>《》/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(shortPinyin);
        return m.replaceAll(" ").trim();
    }

	/**
	 * 替换特殊字符
	 * @param shortPinyin 字符串变量
	 * @param replaceStr 替换的字符串
	 * @return 返回被替换的字符串
	 */
	public static String replaceSpecialCharacters(String shortPinyin, String replaceStr) {
		String regEx = "[-`~!@#$%^&*()+=|{}':;',\\[\\]\".<>《》/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s*]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(shortPinyin);
		return m.replaceAll(replaceStr).trim();
	}
    
    /**
     * @param str
     * @return
     */
    public static String base64Encode(String str) {
        if (StringUtil.isEmpty(str)) return StringUtil.EMPTY;
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    /**
     * @param str
     * @return
     */
    public static String base64Decode(String str) {
        if (StringUtil.isEmpty(str)) return StringUtil.EMPTY;
        byte[] bt;
        try {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            bt = decoder.decodeBuffer(str);
            str = new String(bt, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 字符串截取 比如：/2014/11/10/12/
     * 截取到第几个/为止
     *
     * @param str   要截取的字符串
     * @param split 分隔符
     * @param index 第几个
     * @return
     */
    public static String substring(String str, String split, int index) {
        if (StringUtil.isEmpty(str) ||
                StringUtil.isEmpty(split) || index < 0) return StringUtil.EMPTY;
        String tmp[] = str.split(split);
        StringBuilder sb = new StringBuilder();
        if (index > tmp.length) return StringUtil.EMPTY;

        for (int i = 0; i < tmp.length; i++) {
            if (i < index) sb.append(tmp[i]).append(split);
        }
        tmp = null;
        return sb.toString();
    }

	/**
	 * 首字母转大写
	 * @param s 待转换的字符窜
	 * @return String 转化后的
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			StringBuilder sb = new StringBuilder(s);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		}
	}
	
	/**
	 * 下划线转驼峰风格
	 * 
	 * @param underscoreName 下划线名称
	 * @return 驼峰风格的字符串
	 */
	public static String underscore2camel(String underscoreName){
		if(isEmpty(underscoreName)){
			return EMPTY;
		}
		
		String[] sections = underscoreName.toLowerCase().split("_");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<sections.length;i++){
			String s = sections[i];
			if(i==0){
				sb.append(s);
			}else{
				sb.append(capitalize(s));
			}
		}
		return sb.toString();
	}
}
