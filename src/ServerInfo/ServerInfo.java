package ServerInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

public class ServerInfo extends JavaPlugin {
	
	Logger log;
	List<WrappedGameProfile> Text = new ArrayList<WrappedGameProfile>();
	
	
	public void onEnable(){
		log = Logger.getLogger("Minecraft");
		log.info("GuiServerInfo activating...");
		ProtocolLibrary.getProtocolManager().addPacketListener(
			new PacketAdapter(this, ListenerPriority.NORMAL,
			Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
				@Override
				public void onPacketSending(PacketEvent event) {
					handlePing(event.getPacket().getServerPings().read(0));
				}
			}
		);
		this.saveDefaultConfig();
		FileConfiguration config = getConfig();
		for (int i = 0; i < config.getStringList("Text").size();i++){
			Text.add(
					new WrappedGameProfile(
							"id" + i + 1,
							config.getStringList("Text").get(i))
					);
			}
		log.info("GuiServerInfo active!");
	}
	private void handlePing(WrappedServerPing ping) {
		ping.setPlayers(Text);
	}
	
	public void onDisable(){
		log.info("GuiServerInfo disabled!");
	}

}
