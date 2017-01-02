package com.kolian.ipblock;

import org.apache.commons.lang3.StringUtils;

/**
 * ip패턴에 따라 범위를 저장할 클래스
 * Created by kolian on 2016. 12. 28..
 */
public class IpRange {
    private static final int IPV4_BIT_COUNT = 32;
    private long from;
    private long to;

    public IpRange(String ipPattern) {
        convert(ipPattern);
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public void setTo(long to) {
        this.to = to;
    }

    /**
     * ipPattern을 입력 받아서 범위로 표현
     *
     * @param ipPattern
     */
    private void convert(String ipPattern) {
        if (StringUtils.isEmpty(ipPattern)) {
            throw new IllegalArgumentException("IpPattern cannot be null");
        }

        String ip = ipPattern;
        long maskBitCount = IPV4_BIT_COUNT;
        if (ipPattern.indexOf("/") > 0) {
            String[] addressAndMask = ipPattern.split("/");
            ip = addressAndMask[0];
            maskBitCount = Long.parseLong(addressAndMask[1]);
        } else if (ipPattern.indexOf('*') > 0) {
            maskBitCount = IPV4_BIT_COUNT - (StringUtils.countMatches(ipPattern, "*") * 8);
            ip = ipPattern.replaceAll("\\*", "255");
        }

        String[] splitIps = ip.split("\\.");
        for (String splitIp : splitIps) {
            from = from << 8;
            from |= Long.parseLong(splitIp);
        }

        to = from;
        long mask = 0xFFFFFFFFL >>> maskBitCount;

        to = to | mask;
        from = from & (~mask);

    }
}
