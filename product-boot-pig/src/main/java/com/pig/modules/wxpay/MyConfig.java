package com.pig.modules.wxpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

    private String certPath = "D:\\Program Files\\Downloads\\WXCertUtil\\cert\\1628655062_20220717_cert\\apiclient_cert.p12";

    // 生产环境用这个
//    private String certPath = "/home/publish/cert/apiclient_cert.p12";

    private String appId = "wx98c69df355fad828";

    private String mchID = "1628655062";

    private String v2Key = "FLKtRVXq75okuzRnzXFh2by00FdBSma5";


    private byte[] certData;

    public MyConfig() throws Exception {
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return appId;
    }

    public String getMchID() {
        return mchID;
    }

    public String getKey() {
        return v2Key;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
}
