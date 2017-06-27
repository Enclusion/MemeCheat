package me.joeleoli.memecheat.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import me.joeleoli.memecheat.protocol.adapter.*;

import org.bukkit.plugin.Plugin;

public class ProtocolLibHook {

    private ProtocolManager protocolManager;

    public ProtocolLibHook(Plugin plugin) {
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new FlyAdapter(plugin));
        protocolManager.addPacketListener(new HeldItemAdapter(plugin));
        protocolManager.addPacketListener(new InteractAdapter(plugin));
        protocolManager.addPacketListener(new KeepAliveAdapter(plugin));
        protocolManager.addPacketListener(new LookAdapter(plugin));
        protocolManager.addPacketListener(new PositionAdapter(plugin));
        protocolManager.addPacketListener(new PositionLookAdapter(plugin));
        protocolManager.addPacketListener(new SwingAdapter(plugin));
        protocolManager.addPacketListener(new UseEntityAdapter(plugin));
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
    
}