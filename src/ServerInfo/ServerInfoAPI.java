package ServerInfo;

import java.util.ArrayList;
import java.util.List;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class ServerInfoAPI extends ServerInfo {
	
	public void setServerInfo(List<WrappedGameProfile> Text){
		super.Text = Text;
		super.log.info("Set new ServerInfo");
	}
	
	public void setServerInfo(String[] Text){
		List<WrappedGameProfile> tmp = new ArrayList<WrappedGameProfile>();
		for (int i = 0; i < Text.length; i++) {
			tmp.set(i, new WrappedGameProfile("id" + i + 1, ConvertFormat(Text[i])));
		}
		super.Text = tmp;
		super.log.info("Set new ServerInfo");
	}
	
	public void insertServerInfoString(String str, int num){
		Text.set(num, new WrappedGameProfile("id" + num, ConvertFormat(str)));
		super.log.info("Add new ServerInfo - num:" + num + ", Text:" + str);
	}
	
	public void addServerInfoString(String str){
		Text.add(new WrappedGameProfile("id" + Text.size() + 1, ConvertFormat(str)));
		super.log.info("Add new ServerInfo: " + str);
	}
	
	public int getServerInfoSize(){
		return Text.size();
	}
	
	public String getServerInfoString(int num){
		return Text.get(num).getName();
	}
	
	public int getServerInfoStringNum(WrappedGameProfile pr){
		return Text.indexOf(pr);
	}

}
