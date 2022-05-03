package xyz.winston.nettytransporter.protocol.packet;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.NonNull;
import lombok.val;

public class PacketMapper {

    private final PacketFactory[] idFactoryMap
            = new PacketFactory[256];

    private final TObjectIntMap<Class<?>> classIdMap
            = new TObjectIntHashMap<>(256, 0.5F, -1);

    public void registerPacket(
            final int id,
            final @NonNull Class<?> cls,
            final @NonNull PacketFactory factory
    ) {
        idFactoryMap[id] = factory;
        classIdMap.put(cls, id);
    }

    public Packet<?> newPacket(final int id) {
        if (id >= idFactoryMap.length) return null;

        val supplier = idFactoryMap[id];
        if (supplier == null) return null;

        return supplier.newInstance();
    }

    public int getPacketId(Class<?> cls) {
        return classIdMap.get(cls);
    }

}
