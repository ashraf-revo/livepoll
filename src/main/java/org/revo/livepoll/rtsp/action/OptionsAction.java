package org.revo.livepoll.rtsp.action;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspVersions;
import org.revo.livepoll.rtsp.RtspSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.revo.livepoll.rtsp.utils.MessageUtils.*;


public class OptionsAction extends BaseAction<DefaultFullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(OptionsAction.class);

    public OptionsAction(DefaultFullHttpRequest req, RtspSession rtspSession) {
        super(req, rtspSession);
    }

    @Override
    public DefaultFullHttpResponse call() {
        DefaultFullHttpResponse rep = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, HttpResponseStatus.OK);
        get(req, RtspHeaderNames.CSEQ).ifPresent(it -> append(rep, it));
        get(req, RtspHeaderNames.SESSION).ifPresent(it -> append(rep, it));
        set(RtspHeaderNames.PUBLIC, "SETUP, ANNOUNCE, TEARDOWN, RECORD").ifPresent(it -> append(rep, it));
        set(RtspHeaderNames.SERVER, "aslive").ifPresent(it -> append(rep, it));
        set(RtspHeaderNames.CONTENT_LENGTH, "0").ifPresent(it -> append(rep, it));
        return rep;
    }

}
