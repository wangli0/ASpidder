package com.less.aspider.downloader;

import com.less.aspider.bean.Page;
import com.less.aspider.bean.Proxy;
import com.less.aspider.bean.Request;
import com.less.aspider.http.HttpConnUtils;
import com.less.aspider.proxy.ProxyProvider;

/**
 * Created by deeper on 2017/12/21.
 */

public class HttpConnDownloader implements Downloader {

    private ProxyProvider proxyProvider;

    @Override
    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    @Override
    public Page download(Request request) {
        Page page = new Page();
        Proxy proxyBean = null;
        byte[] bytes = null;
        if (proxyProvider != null && (proxyBean = proxyProvider.getProxy()) != null) {
            System.out.println("======> Request Proxy: " + proxyBean.getHost() + " : " + proxyBean.getPort());
            bytes = HttpConnUtils.getDefault().sendRequestByProxy(request.getUrl(),proxyBean.getHost(),proxyBean.getPort());
        } else {
            System.out.println("=====> Request Nomal <=====");
            bytes = HttpConnUtils.getDefault().sendRequest(request.getUrl());
        }
        if (null != bytes) {
            page.setUrl(request.getUrl());
            page.setRawText(new String(bytes));
            page.setDownloadSuccess(true);
        } else {
            page.setDownloadSuccess(false);
        }
        if (proxyProvider != null && proxyBean != null) {
            proxyProvider.returnProxy(proxyBean,page);
        }
        return page;
    }
}
