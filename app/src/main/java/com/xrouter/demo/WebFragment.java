package com.xrouter.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xfragment.RootFragment;
import com.xrouter.XRouter;

public class WebFragment extends RootFragment {

	private WebView mWebView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mWebView = new WebView(getContext());
		mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mWebView.setWebViewClient(new DefaultWebViewClient());
		mWebView.getSettings().setUseWideViewPort(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		return mWebView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		Bundle extras = getArguments();
//		if (extras != null) {
//			extras.putAll(getActivity().getIntent().getExtras());
//		}
		String url = "file:///android_asset/web.html";
//		if (extras != null) {
//			url = extras.getString("url", url);
//		}
		mWebView.loadUrl(url);
	}

	private class DefaultWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("xrouter")) {
                new XRouter.XRouterBuilder()
                        .from(WebFragment.this)
                        .to(url)
                        .start();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			super.onReceivedError(view, request, error);
		}
	}
}
