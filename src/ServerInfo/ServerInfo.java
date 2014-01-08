package ServerInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	    return format.replace("#0", ""+ChatColor.BLACK).replace("#1", ""+ChatColor.DARK_BLUE).replace("#2", ""+ChatColor.DARK_GREEN).replace("#3", ""+ChatColor.DARK_AQUA).replace("#4", ""+ChatColor.DARK_RED).replace("#5", ""+ChatColor.DARK_PURPLE).replace("#6", ""+ChatColor.GOLD).replace("#7", ""+ChatColor.GRAY).replace("#8", ""+ChatColor.DARK_GRAY).replace("#9", ""+ChatColor.BLUE).replace("#a", ""+ChatColor.GREEN).replace("#b", ""+ChatColor.AQUA).replace("#c", ""+ChatColor.RED).replace("#d", ""+ChatColor.LIGHT_PURPLE).replace("#e", ""+ChatColor.YELLOW).replace("#f", ""+ChatColor.WHITE);
	}
	
	
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
		config = getConfig();
		for (int i = 0; i < config.getStringList("Text").size();i++){
			Text.add(
					new WrappedGameProfile(
							"id" + i + 1,
							ConvertFormat(config.getStringList("Text").get(i)))
					);
			}
		log.info("GuiServerInfo active!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.isOp()){
			if (args.length > 0){
				if (args[0].equalsIgnoreCase("set")){
					Text.clear();
					List<String> newConf = new ArrayList<String>();
					for (int i = 1; i < args.length; i++){
						Text.add(new WrappedGameProfile("id" + Text.size() + 1, ConvertFormat(args[i])));
						newConf.add(args[i]);
						log.info("Add ServerInfo: " + args[i]);
					}
					config.set("Text", newConf);
					sender.sendMessage("Successful!");
					return true;
				}
			}
			return false;
		} else {
			sender.sendMessage("You not op!");
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

}
