package me.aboullaite.edgeservice.config;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import javax.servlet.ServletException;
import java.io.IOException;

class SleuthValve extends ValveBase {
    private final Tracer tracer;

    public SleuthValve(Tracer tracer){
        this.tracer = tracer;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

        enrichWithSleuthHeaderWhenMissing(tracer, request);

        Valve next = getNext();
        if (null == next) {
            // no next valve
            return;
        }
        next.invoke(request, response);
    }

    private static void enrichWithSleuthHeaderWhenMissing(final Tracer tracer, final Request request) {
        String header = request.getHeader(Span.TRACE_ID_NAME);
        if (null == header) {

            org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
            MimeHeaders mimeHeaders = coyoteRequest.getMimeHeaders();

            Span span = tracer.createSpan("SleuthValve");

            addHeader(mimeHeaders, Span.TRACE_ID_NAME, span.traceIdString());
            addHeader(mimeHeaders, Span.SPAN_ID_NAME, span.traceIdString());
        }
    }

    private static void addHeader(MimeHeaders mimeHeaders, String traceIdName, String value) {
        MessageBytes messageBytes = mimeHeaders.addValue(traceIdName);
        messageBytes.setString(value);
    }
}
