package org.spacehq.mc.protocol1_7_7;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.SessionService;
import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.auth.exception.AuthenticationUnavailableException;
import org.spacehq.mc.auth.exception.InvalidCredentialsException;
import org.spacehq.mc.protocol1_7_7.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol1_7_7.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol1_7_7.data.status.handler.ServerPingTimeHandler;
import org.spacehq.mc.protocol1_7_7.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol1_7_7.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol1_7_7.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol1_7_7.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol1_7_7.packet.login.client.EncryptionResponsePacket;
import org.spacehq.mc.protocol1_7_7.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol1_7_7.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol1_7_7.packet.login.server.LoginDisconnectPacket;
import org.spacehq.mc.protocol1_7_7.packet.login.server.LoginSuccessPacket;
import org.spacehq.mc.protocol1_7_7.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol1_7_7.packet.status.client.StatusQueryPacket;
import org.spacehq.mc.protocol1_7_7.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol1_7_7.packet.status.server.StatusResponsePacket;
import org.spacehq.mc.protocol1_7_7.util.CryptUtil;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

import eu.matejkormuth.staticmc.ProtocolMode;

import javax.crypto.SecretKey;

import java.math.BigInteger;

public class ClientListener extends SessionAdapter {

	private SecretKey key;

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		MinecraftProtocol_177 protocol = (MinecraftProtocol_177) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			if(event.getPacket() instanceof EncryptionRequestPacket) {
				EncryptionRequestPacket packet = event.getPacket();
				this.key = CryptUtil.generateSharedKey();
				
				GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
				String serverHash = new BigInteger(CryptUtil.getServerIdHash(packet.getServerId(), packet.getPublicKey(), this.key)).toString(16);
				String accessToken = event.getSession().getFlag(ProtocolConstants.ACCESS_TOKEN_KEY);
				try {
					new SessionService().joinServer(profile, accessToken, serverHash);
				} catch(AuthenticationUnavailableException e) {
					event.getSession().disconnect("Login failed: Authentication service unavailable.");
					return;
				} catch(InvalidCredentialsException e) {
					event.getSession().disconnect("Login failed: Invalid login session.");
					return;
				} catch(AuthenticationException e) {
					event.getSession().disconnect("Login failed: Authentication error: " + e.getMessage());
					return;
				}
	
				event.getSession().send(new EncryptionResponsePacket(this.key, packet.getPublicKey(), packet.getVerifyToken()));
			} else if(event.getPacket() instanceof LoginSuccessPacket) {
				LoginSuccessPacket packet = event.getPacket();
				event.getSession().setFlag(ProtocolConstants.PROFILE_KEY, packet.getProfile());
				protocol.setMode(ProtocolMode.GAME, true, event.getSession());
			} else if(event.getPacket() instanceof LoginDisconnectPacket) {
				LoginDisconnectPacket packet = event.getPacket();
				event.getSession().disconnect(packet.getReason().getFullText());
			}
		}
		
		if(protocol.getMode() == ProtocolMode.STATUS) {
			if(event.getPacket() instanceof StatusResponsePacket) {
				ServerStatusInfo info = event.<StatusResponsePacket>getPacket().getInfo();
				ServerInfoHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY);
				if(handler != null) {
					handler.handle(event.getSession(), info);
				}
				
				event.getSession().send(new StatusPingPacket(System.nanoTime() / 1000000));
			} else if(event.getPacket() instanceof StatusPongPacket) {
				long time = System.nanoTime() / 1000000 - event.<StatusPongPacket>getPacket().getPingTime();
				ServerPingTimeHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY);
				if(handler != null) {
					handler.handle(event.getSession(), time);
				}
				
				event.getSession().disconnect("Finished");
			}
		}
		
		if(protocol.getMode() == ProtocolMode.GAME) {
			if(event.getPacket() instanceof ServerKeepAlivePacket) {
				event.getSession().send(new ClientKeepAlivePacket(event.<ServerKeepAlivePacket>getPacket().getPingId()));
			} else if(event.getPacket() instanceof ServerDisconnectPacket) {
				event.getSession().disconnect(event.<ServerDisconnectPacket>getPacket().getReason().getFullText());
			}
		}
	}
	
	@Override
	public void packetSent(PacketSentEvent event) {
		MinecraftProtocol_177 protocol = (MinecraftProtocol_177) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN && event.getPacket() instanceof EncryptionResponsePacket) {
			protocol.enableEncryption(this.key);
		}
	}

	@Override
	public void connected(ConnectedEvent event) {
		MinecraftProtocol_177 protocol = (MinecraftProtocol_177) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
			protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
			event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), 2));
			protocol.setMode(ProtocolMode.LOGIN, true, event.getSession());
			event.getSession().send(new LoginStartPacket(profile != null ? profile.getName() : ""));
		} else if(protocol.getMode() == ProtocolMode.STATUS) {
			protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
			event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), 1));
			protocol.setMode(ProtocolMode.STATUS, true, event.getSession());
			event.getSession().send(new StatusQueryPacket());
		}
	}

}
