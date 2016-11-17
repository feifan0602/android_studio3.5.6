package com.sun3d.culturalShanghai.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

public class ValidateUtil {
	// 注册验证
	public static boolean registerVaildate(String userNickName, String userPwd,
			String phone, String code, StringBuffer sb) {
		if (!isCellphone(phone, sb)) {// 手机号
			// sb.append(MyApplication.getContext().getString(R.string.register_null_phone));
			return false;
		}
		if (isEmpty(userNickName)) {// 昵称
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_name));
			return false;
		}
		if (!isIllegal(userNickName)) {
			sb.append("不能输入特殊字符");
			return false;
		}

		if (isEmpty(userPwd)) {// 密码
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_pw));
			return false;
		}
		if (!isPwd(userPwd, sb)) {

			return false;
		}

		if (isEmpty(code)) {// 手机验证码
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_code));
			return false;
		}
		// if (!isPwd(userPwd)) {
		// sb.append(MyApplication.getContext().getString(R.string.user_null_name));
		// return false;
		// }
		return true;
	}

	/**
	 * 用户登录判断 看填写的信息是否正确
	 * 
	 * @param userName
	 * @param pw
	 * @param sb
	 * @return
	 */
	public static boolean loginVaildate(String userName, String pw,
			StringBuffer sb) {
		if (isEmpty(userName)) {
			sb.append(MyApplication.getContext().getString(
					R.string.register_null_phone));
			return false;
		}
		if (!isCellphone(userName, sb)) {
			// sb.append(MyApplication.getContext().getString(R.string.register_phone_erorr));
			return false;
		}
		if (isEmpty(pw)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_pw));
			return false;
		}
		if (!isPwd(pw, sb)) {
			// sb.append(MyApplication.getContext().getString(R.string.user_pw_error));
			return false;
		}
		return true;
	}

	/**
	 * 修改密码
	 */
	public static boolean findpwVaildate(String sOld, String sNewPw,
			String sPw, StringBuffer sb) {
		if (isEmpty(sOld)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_old_pw));
			return false;
		}
		if (isEmpty(sNewPw)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_new_pw));
			return false;
		}
		if (isEmpty(sPw)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_que_pw));
			return false;
		}
		if (sOld.equals(sNewPw)) {
			sb.append("原密码与新密码一致");
			return false;
		}
		if (!(isPwd(sOld, sb) && isPwd(sNewPw, sb) && isPwd(sPw, sb))) {
			// sb.append(MyApplication.getContext().getString(R.string.user_pw_error));
			return false;
		}
		if (!sNewPw.equals(sPw)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_two_pw_erorr));
			return false;
		}
		return true;
	}

	public static boolean updateName(String name, StringBuffer sb) {
		if (isEmpty(name)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_null_name));
			return false;
		}
		return true;

	}

	/**
	 * 找回密码
	 */
	public static boolean findpwVaildate(String sOld, String sNewPw,
			StringBuffer sb) {
		if (isEmpty(sOld)) {
			sb.append("密码不能为空");
			return false;
		}
		if (isEmpty(sNewPw)) {
			sb.append("确认密码不能为空");
			return false;
		}
		if (!(isPwd(sOld, sb) && isPwd(sNewPw, sb))) {
			// sb.append(MyApplication.getContext().getString(R.string.user_pw_error));
			return false;
		}
		if (!sNewPw.equals(sOld)) {
			sb.append(MyApplication.getContext().getString(
					R.string.user_two_pw_erorr));
			return false;
		}
		return true;
	}

	// 判断是否是呢称
	public static boolean isNickUser() {

		return false;
	}

	/**
	 * 判断是否是合法手机号码
	 * 
	 * @param cellphone
	 * @return
	 */
	public static boolean isCellphone(String cellphone, StringBuffer sb) {
		if (isEmpty(cellphone)) {
			sb.append(MyApplication.getContext().getString(
					R.string.register_null_phone));
			return false;
		}
		String regex = "^((13[0-9])|(14[0-9])|(15([0-9]))|(17([0-9]))|(18[0-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(cellphone);
		if (!m.matches()) {
			sb.append(MyApplication.getContext().getString(
					R.string.register_phone_erorr));
			return false;
		}
		return true;
	}

	/**
	 * 判断是否有空格
	 * 
	 * @param value
	 * @return
	 */

	public static boolean isBlank(String value) {
		int x = value.indexOf(" ");
		if (x == -1) {
			return false;
		} else {
			return true;
		}
	}

	// 是否有非法字符
	public static boolean isIllegal(String value) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z0-9_]+$");
		Matcher m = p.matcher(value);
		return m.matches();
	}

	// 判断是否是用户(邮箱)
	public static boolean isEmail(String value) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 判断是否纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	// 判断是否是密码
	public static boolean isPwd(String value, StringBuffer sb) {
		if (value != null) {
			try {

				if (value.length() < 6 || value.length() > 12) {
					sb.append(MyApplication.getContext().getString(
							R.string.user_pw_error));
					return false;
				}
				if (!isIllegal(value)) {
					sb.append("密码仅支持数字加字母的组合");
					return false;
				}
				Pattern pattern = Pattern
						.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
				// Pattern pattern = Pattern.compile("[0-9a-zA-Z]{6,12}");
				Matcher matcher = pattern.matcher(value);
				if (matcher.matches()) {
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sb.append("密码仅支持数字加字母的组合");
		return false;
	}

	// 判断是否为空
	public static boolean isEmpty(String text) {
		if (text == null || text.length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
