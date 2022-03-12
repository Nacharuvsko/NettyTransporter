package xyz.winston.nettytransporter.protocol.packet;

import java.util.Iterator;

/**
 * @author winston
 */
public abstract class BossProcessor implements PacketProcessor, Iterable<PacketProcessor> {

    private ProcNode first;
    private ProcNode last;

    @Override
    public final Iterator<PacketProcessor> iterator() {
        return new PacketProcessorIterator(first);
    }

    public final void callActive() throws Exception {
        active();

        for (PacketProcessor processor : this) {
            processor.active();
        }
    }

    public final void callException(Throwable cause) throws Exception {
        process(cause);

        for (PacketProcessor processor : this) {
            processor.process(cause);
        }
    }

    public final void callInactive() throws Exception {
        inactive();

        for (PacketProcessor processor : this) {
            processor.inactive();
        }
    }

    public final void callProcessor(ChannelProcessorContext ctx) throws Exception {
        Packet<?> packet = ctx.getPacket();

        if (packet.isProcessor(this)) {
            process(ctx);
        }

        for (PacketProcessor processor : this) {
            if (packet.isProcessor(processor)) {
                processor.process(ctx);
            }
        }
    }

    public final void registerProcessor(PacketProcessor processor) {
        ProcNode node = new ProcNode(processor);

        if (last == null) {
            first = node;
        } else {
            last.next = node;
            node.prev = last;
        }

        last = node;
    }

    static class PacketProcessorIterator implements Iterator<PacketProcessor> {

        final ProcNodeIterator iterator;

        public PacketProcessorIterator(ProcNode node) {
            this.iterator = new ProcNodeIterator(node);
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public PacketProcessor next() {
            return iterator.next().processor;
        }

    }

    static class ProcNodeIterator implements Iterator<ProcNode> {
        final ProcNode head;
        ProcNode cur;

        public ProcNodeIterator(ProcNode node) {
            this.head = node;
        }

        @Override
        public boolean hasNext() {
            return cur == null ? head != null : cur.next != null;
        }

        @Override
        public ProcNode next() {
            if (cur == null) {
                cur = head;
            } else {
                cur = cur.next;
            }

            return cur;
        }

    }

    static class ProcNode {

        ProcNode next;
        ProcNode prev;

        final PacketProcessor processor;

        public ProcNode(PacketProcessor processor) {
            this.processor = processor;
        }

    }

}
