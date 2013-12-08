package com.keju.baby.activity.base;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.baby.BabyMainActivity;

/**
 * html页面
 * @author Zhoujun
 * @version 创建时间：2013-11-21 下午2:42:53
 */
public class BaseWebViewActivity extends BaseActivity {
	protected ImageView btnLeft, btnRight;
	protected TextView tvTitle;
	protected WebView webView;
	protected View viewWebTab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webview);
		init();
	}
	protected void init(){
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		viewWebTab = findViewById(R.id.viewWebTab);
		
		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.requestFocus();// 使WebView内的输入框等获得焦点
		webView.setWebViewClient(new WebViewClient() {
			// 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// 可以让webView处理https请求
			@Override
			public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler,
					android.net.http.SslError error) {
				handler.proceed();
			};

			public void onLoadResource(WebView view, String url) {

			};
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				setWebTabVisible(url);
				if(url.contains(Constants.URL_NEED)){
					BabyMainActivity.setTabVisible(false);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}else{
					BabyMainActivity.setTabVisible(true);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				if(changeTitle(url)){
//					tvTitle.setText(view.getTitle());
//				}
				tvTitle.setText(view.getTitle());
				btnLeftIsVisible(url);
				btnRightIsVisible(url);
			}
		});
	}
	protected void loadUrl(String url) {
		webView.loadUrl(Constants.URL_BASE_HTML + url);
	}
	private static final String btnLeftInVisibleUrls[] = {Constants.URL_FITMENT_LIST,Constants.URL_MEETING_NOTIFY_LIST,Constants.URL_DOCTOR_LOGIN,Constants.URL_BABY_LOGIN };
	private static final String btnLeftVisibleUrls[] = {Constants.URL_FITMENT_DETAIL,Constants.URL_MEETING_NOTIFY_DETAIL,Constants.URL_REGISTER,Constants.URL_ADD_FOLLOW_UP,Constants.URL_NEED };
	private static final String babyDetailUrls[] = {Constants.URL_VISIT_RECORD,Constants.URL_GROW_LINE,Constants.URL_GROW_RATE,Constants.URL_BABY_DETAIL};
	private static final String btnRightVisibleUrls[] = {Constants.URL_VISIT_RECORD,Constants.URL_GROW_LINE,Constants.URL_GROW_RATE,Constants.URL_BABY_DETAIL};
	/**
	 * 左边的按钮是否可见
	 * @param url
	 */
	private void btnLeftIsVisible(String url){
		for (int i = 0; i < btnLeftInVisibleUrls.length; i++) {
			if(url.contains(btnLeftInVisibleUrls[i])){
				btnLeft.setVisibility(View.INVISIBLE);
				break;
			}
		}
		for (int i = 0; i < btnLeftVisibleUrls.length; i++) {
			if(url.contains(btnLeftVisibleUrls[i])){
				btnLeft.setVisibility(View.VISIBLE);
				break;
			}
		}
		for (int i = 0; i < babyDetailUrls.length; i++) {
			if(url.contains(babyDetailUrls[i])){
				btnLeft.setVisibility(View.VISIBLE);
				break;
			}
		}
	}
	/**
	 * 右边的按钮是否可见
	 * @param url
	 */
	private void btnRightIsVisible(String url){
		if(url.contains(Constants.URL_NEED) ||url.contains(Constants.URL_FORMULA)){
			btnRight.setVisibility(View.INVISIBLE);
		}
		for (int i = 0; i < btnRightVisibleUrls.length; i++) {
			if(url.contains(btnRightVisibleUrls[i])){
				btnRight.setVisibility(View.VISIBLE);
			}
		}
	}
	/**
	 * 婴儿详情
	 * @param url
	 */
	private boolean changeTitle(String url){
		boolean isChangeTitle = true;
		for (int i = 0; i < babyDetailUrls.length; i++) {
			if(url.contains(babyDetailUrls[i])){
				isChangeTitle = false;
				break;
			}
		}
		if(url.contains(Constants.URL_NEED)){
			isChangeTitle = true;
		}
		return isChangeTitle;
	}
	/**
	 * 设置web导航是否可见
	 */
	private void setWebTabVisible(String url){
		boolean isVisible = false;;
		for (int i = 0; i < babyDetailUrls.length; i++) {
			if(url.contains(babyDetailUrls[i])){
				isVisible = true;
				break;
			}
		}
		if(isVisible){
			viewWebTab.setVisibility(View.VISIBLE);
		}else{
			viewWebTab.setVisibility(View.GONE);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		webView.clearCache(true);
		webView.clearHistory();
	}
}
