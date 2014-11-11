package ServerInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	protected List<WrappedGameProfile> Text = new ArrayList<WrappedGameProfile>();
	FileConfiguration config;
	
	protected String ConvertFormat(String format){
	    return ChatColor.translateAlternateColorCodes('#', format);
	}
	
	
	public void onEnable(){
		log = getLogger();
		log.info("Activating...");
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
		config = getConfig();
		for (int i = 0; i < config.getStringList("Text").size();i++){
			Text.add(
					getWrappedGameProfile(
							"id" + i + 1,
							ConvertFormat(config.getStringList("Text").get(i)))
					);
			}
		log.info("GuiServerInfo is active!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.isOp()){
			if (args.length > 0){
				if (args[0].equalsIgnoreCase("set")){
					Text.clear();
					List<String> newConf = new ArrayList<String>();
					for (int i = 1; i < args.length; i++){
						Text.add(getWrappedGameProfile("id" + Text.size() + 1, ConvertFormat(args[i])));
						newConf.add(args[i]);
						log.info("Add ServerInfo: " + args[i]);
					}
					config.set("Text", newConf);
					sender.sendMessage("Successful!");
					return true;
				}
			}
			return true;
		} else {
			sender.sendMessage("You are not op!");
			return true;
		}
	}
	
	private void handlePing(WrappedServerPing ping) {
		ping.setPlayers(Text);
	}
	
	public void onDisable(){
		saveConfig();
		log.info("GuiServerInfo disabled!");
	}

	public WrappedGameProfile getWrappedGameProfile(String num, String str) {
		return new WrappedGameProfile(UUID.nameUUIDFromBytes(num.getBytes()), str);
	}
	
}
