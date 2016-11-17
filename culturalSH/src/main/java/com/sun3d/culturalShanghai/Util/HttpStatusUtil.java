package com.sun3d.culturalShanghai.Util;

/**
 * http状态码解析
 * 
 * @author yangyoutao
 * 
 */
public class HttpStatusUtil {
	public static String httpStatus(int code) {
		String result = null;
		switch (code) {
		case 400:
			result = "code:400,错误请求";
			break;
		case 401:
			result = "code:401,未授权";
			break;
		case 403:
			result = "code:403,服务器拒绝请求";
			break;
		case 404:
			result = "code:404,找不到请求的页面";
			break;
		case 405:
			result = "code:405,方法禁用";
			break;
		case 406:
			result = "code:406,不接受的请求";
			break;
		case 408:
			result = "code:408,请求超时";
			break;
		case 409:
			result = "code:409,冲突";
			break;
		case 410:
			result = "code:410,资源已永久删除";
			break;
		case 415:
			result = "code:415,不支持的媒体类型";
			break;
		case 500:
			result = "code:500,服务器错误";
			break;
		case 501:
			result = "code:501,服务器无法识别请求授权";
			break;
		case 502:
			result = "code:502,错误网关";
			break;
		case 503:
			result = "code:503,服务不可用";
			break;
		case 504:
			result = "code:504,HTTP 版本不受支持";
			break;
		default:
			result = "code:" + code + ",错误。";
			break;
		}
		return result;

	}

}
