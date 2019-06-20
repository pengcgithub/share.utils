package com.platform.common.network;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP工具类<br/>
 * 对于各种HTTP请求操作处理
 *
 * @author pengc
 * @see com.share.network
 * @since 2017/6/26
 */
public class HttpUtil {

    /** IE浏览器标识 **/
    private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};

    /**
     * 判断是否为IE浏览器<br>
     * 通过使用Trident和Edge关键字来判断是否是微软的浏览器
     *
     * @param request
     * @return 返回true 为IE浏览器，否则为其他浏览器
     * @since 2017/6/26
     */
    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal))
                return true;
        }
        return false;
    }
}
