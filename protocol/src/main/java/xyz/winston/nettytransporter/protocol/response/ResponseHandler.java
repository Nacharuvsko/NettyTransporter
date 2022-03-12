package xyz.winston.nettytransporter.protocol.response;

import xyz.winston.nettytransporter.protocol.packet.Packet;

@SuppressWarnings("rawtypes")
public interface ResponseHandler<Response extends Packet> {

    default void handleCause(Throwable cause) {
        cause.printStackTrace();
    }

    void handleResponse(Response response);

    @SuppressWarnings("unchecked")
    default ResponseHandler<Packet> makeUnchecked() {
        ResponseHandler<Response> old = this;

        return new ResponseHandler<Packet>() {
            @Override
            public void handleResponse(Packet packet) {
                old.handleResponse((Response) packet);
            }

            @Override
            public void handleCause(Throwable cause) {
                old.handleCause(cause);
            }
        };
    }

}
