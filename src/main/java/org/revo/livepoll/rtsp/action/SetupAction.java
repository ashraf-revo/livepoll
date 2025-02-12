package org.revo.livepoll.rtsp.action;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspVersions;
import org.revo.livepoll.rtsp.RtspSession;
import org.revo.livepoll.rtsp.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.TransportNotSupportedException;
import java.util.AbstractMap;
import java.util.Optional;

import static org.revo.livepoll.rtsp.utils.MessageUtils.append;
import static org.revo.livepoll.rtsp.utils.MessageUtils.get;


public class SetupAction extends BaseAction<DefaultFullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(SetupAction.class);

    public SetupAction(DefaultFullHttpRequest req, RtspSession rtspSession) {
        super(req, rtspSession);
    }

    @Override
    public DefaultFullHttpResponse call() {
        DefaultFullHttpResponse rep = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, HttpResponseStatus.OK);
        get(req, RtspHeaderNames.CSEQ).ifPresent(it -> append(rep, it));
        Optional.of(get(req, RtspHeaderNames.SESSION).orElse(new AbstractMap.SimpleImmutableEntry<>(RtspHeaderNames.SESSION, rtspSession.getId())))
                .ifPresent(it -> append(rep, it));
        get(req, RtspHeaderNames.TRANSPORT)

                .map(it -> new AbstractMap.SimpleImmutableEntry<>(it.getKey(), Transport.parse(it.getValue())))
                .ifPresent(it -> {
                    try {
                        Transport transport = rtspSession.setup(req.uri(), it.getValue());
                        rep.headers().add(it.getKey(), transport.toString());
                    } catch (TransportNotSupportedException e) {
                        System.out.println(e.getMessage());
                    }
                });


        return rep;
    }
}
