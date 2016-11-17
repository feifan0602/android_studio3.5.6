package com.sun3d.culturalShanghai.Util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;

@SuppressLint("SetJavaScriptEnabled")
public class ViewUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
	private static final String js = "javascript:(function(){"
			+ "var objs = document.getElementsByTagName(\"img\"); "
			+ "for(var i=0;i<objs.length;i++)  " + "{"
			+ "    objs[i].onclick=function()  " + "    {  "
			+ "        window.imagelistner.openImage(this.src);  " + "    }  "
			+ "}" + "})()";

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern
				.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 对接html 特殊字符处理
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		htmlStr = htmlStr.replaceAll("&ldquo", "“");
		htmlStr = htmlStr.replaceAll("&rdquo", "”");
		htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		return htmlStr;
	}

	/**
	 * webview设置参数
	 * 
	 * @param webveiw
	 * @param context
	 */
	@SuppressLint("JavascriptInterface")
	public static void setWebViewSettings(WebView webveiw, Context context) {

		webveiw.setWebChromeClient(new WebChromeClient());
		webveiw.getSettings().setJavaScriptEnabled(true);
		webveiw.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局
		webveiw.setScrollBarStyle(0);
		webveiw.setHorizontalScrollBarEnabled(false);
		webveiw.getSettings().setSupportZoom(true);
		webveiw.getSettings().setDefaultFontSize(16);
		webveiw.getSettings().setLoadWithOverviewMode(true);// 适应屏幕
		webveiw.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		int screenDensity = context.getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;

		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomDensity = WebSettings.ZoomDensity.FAR;
			break;
		// default:
		// zoomDensity = WebSettings.ZoomDensity.CLOSE;
		// break;
		}
		webveiw.getSettings().setDefaultZoom(zoomDensity);
		// 添加js交互接口类，并起别名 imagelistner
		webveiw.addJavascriptInterface(new JavascriptInterface(context),
				"imagelistner");
		webveiw.setWebViewClient(new MyWebViewClient(webveiw));
	}

	public static String initContent(String content, Context context) {
		Log.d("newHtmlContent", "content:" + content);
		try {
			InputStream inputStream = context.getResources().getAssets()
					.open("discover.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream), 16 * 1024);
			StringBuilder sBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sBuilder.append(line + "\n");
			}
			String modelHtml = sBuilder.toString();
			inputStream.close();
			reader.close();
			// String strSpan = content.replaceAll("<span([^>]{0,})>",
			// "").trim();// 去除所有的span标签
			String contentNew = modelHtml.replace(
					"<--@#$%discoverContent@#$%-->", content);
			// contentNew = contentNew.replace("href=", "");
			Log.i("ViewUtil", "看看数据==  " + contentNew);
			contentNew = contentNew
					.replace(
							"<--@#$%colorfontsize2@#$%-->",
							"line-height:27px;letter-spacing:2px;font-size:15px;");
			contentNew=contentNew.replaceAll("温馨提示：", "");
			Log.i("ViewUtil", "看看数据000==  " + contentNew);

			// ;
			//
			// line-height:150%;letter-spacing:2px;color:#647378;font-size:14px;text-align:justify;text-justify:inter-ideograph
			Document doc_Dis = Jsoup.parse(contentNew);
			Elements ele_Img = doc_Dis.getElementsByTag("img");
			if (ele_Img.size() != 0) {
				for (Element e_Img : ele_Img) {
					e_Img.attr("style", "width:100%;height:auto");
					Log.d("Element_img", e_Img.attr("src"));
				}
			}
			String newHtmlContent = doc_Dis.toString();
			Log.d("newHtmlContent", "newHtmlContent:" + newHtmlContent);
			return newHtmlContent;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("newHtmlContent", e.toString());

		}
		return content;
	}

	/**
	 * webview显示部分与全部 boo:true为截取内容（内容小于英文120（中文60）字符将显示部分），false为全部显示
	 * 
	 * */
	public static String subString(String content, boolean boo,
			ImageButton detailMore) {
		// Log.i("newHtmlContents", content);
		String newString = "";
		Document doc_Dis = Jsoup.parse(content);
		if (boo) {
			newString = content;
		} else {
			Elements ele_Img = doc_Dis.getElementsByTag("p");
			if (ele_Img != null && ele_Img.size() > 3) {
				Element in = ele_Img.get(3);
				String text = "";
				if (in.toString().indexOf("“") > -1
						&& in.toString().indexOf("”") > -1) {
					text = in.toString().replaceAll("“", "&ldquo;");
					text = text.replaceAll("”", "&rdquo;");
				} else {
					text = in.toString();
				}
				int index = content.indexOf(text);
				Log.d("newHtmlContents",
						index + "----newHtmlContents:" + in.toString());
				if (index > -1) {
					newString = content.substring(0, index);
				} else {
					newString = content.substring(0, content.length() / 3);
				}
				if (detailMore != null) {
					detailMore.setVisibility(View.VISIBLE);
				}

			} else {
				newString = content.trim();
				if (detailMore != null) {
					detailMore.setVisibility(View.GONE);
				}
			}
		}
		return newString;
	}

	// js通信接口

	public static class JavascriptInterface {
		@SuppressWarnings("unused")
		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		@android.webkit.JavascriptInterface
		public void openImage(final String img) {
			Log.d("openImage", "1212---" + img);
			Intent intent = new Intent(context, ImageOriginalActivity.class);
			intent.putExtra("select_imgs", img);
			intent.putExtra("id", 0);
			context.startActivity(intent);
		}
	}

	// 监听
	public static class MyWebViewClient extends WebViewClient {
		private WebView webveiw;

		public MyWebViewClient(WebView mwebveiw) {
			this.webveiw = mwebveiw;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			view.getSettings().setJavaScriptEnabled(true);

			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();

		}

		// 注入js函数监听
		private void addImageClickListner() {
			// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
			webveiw.loadUrl(js);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

	public String setTextFontFamilt(String str) {
		String str_text = str;
		StringBuilder builder = new StringBuilder();
		builder.append("@font-face {");
		builder.append("font-family: 'cfont';");
		builder.append("font-style: normal;");
		builder.append("font-weight: normal;");
		builder.append("src: url('file:///android_asset/1.ttf') format('truetype');}\n");
		//
		builder.append("p {");//
		builder.append(" margin: 0;");//
		builder.append(" text-align: justify;");//
		builder.append("}");
		builder.toString();
		return str_text;
	}
	// 分栏样式
	// builder.append("body.fenlan {");
	// builder.append("-webkit-column-count:2;");
	// builder.append("-webkit-column-gap:20px;");//
	// builder.append("overflow: scroll;");//
	// builder.append("overflow-y:hidden;");//
	// builder.append("font-family:'cfont';");
	// builder.append("margin:0;");
	// builder.append("width:"+mWidth+"px;");//
	// builder.append("width:"+mWebView.getShowWidth()+"px;");
	// builder.append("height:350px;");
	// builder.append("}\n");
	// //无分栏样式
	// builder.append(".font {");//
	// builder.append("font-family:'cfont';");
	// builder.append("}\n");
	// //取字体
	// builder.append("@font-face {");
	// builder.append("font-family: 'cfont';");
	// builder.append("font-style: normal;");
	// builder.append("font-weight: normal;");
	// builder.append("src: url('file:///android_asset/1.ttf') format('truetype');}\n");
	// //
	// builder.append("p {");//
	// builder.append(" margin: 0;");//
	// builder.append(" text-align: justify;");//
	// builder.append("}");
	// return
	// builder.toString(); }

}
