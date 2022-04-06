package xyz.winston.nettytransporter.protocol.packet;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.val;

public class PacketMapper {

    private final PacketFactory[] idFactoryMap
            = new PacketFactory[256];

    private final TObjectIntMap<Class<?>> classIdMap
            = new TObjectIntHashMap<>(256, 0.5F, -1);

    public void registerPacket(int id, Class<?> cls, PacketFactory factory) {
        idFactoryMap[id] = factory;
        classIdMap.put(cls, id);
    }

    public Packet<?> newPacket(int id) {
        if (id >= idFactoryMap.length) return null;

        val supplier = idFactoryMap[id];
        if (supplier == null) return null;

        return supplier.newInstance();
    }

    public int getPacketId(Class<?> cls) {
        return classIdMap.get(cls);
    }

}
