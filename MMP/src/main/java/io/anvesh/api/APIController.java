package io.anvesh.api;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@RestController
public class APIController {
	
	private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8000/MMP/spotify/login/get_user_code");
	private String code = "";
	private String accessToken = "";
	private URI uri;
	
	@Autowired
	private SpotifyProxy spotifyProxy;
	
	//My user id for spotify
	private final String userId = "mqccjfqu3kbmd2u2t476b6nfb";
	
	
	private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			  .setClientId(Keys.spotifyClientId.getKey())
			  .setClientSecret(Keys.spotifyClientSecret.getKey())
			  .setRedirectUri(redirectUri)
			  .build();

	@GetMapping("/MMP/spotify/login")
	public URI login(HttpServletResponse response) {
		AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
				.show_dialog(true)
				.scope("playlist-modify-public, playlist-modify-private")
				.build();
		uri = authorizationCodeUriRequest.execute();
//		return "Client id: "+spotifyApi.getClientId()+"\nUri:  "+uri.toString();
		try {
			response.sendRedirect(uri.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}
	
	@GetMapping("/MMP/spotify/login/get_user_code")
	public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) {
		code = userCode;
		System.out.println("Code: "+code);
		AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
		try {
			AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
			spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
			spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
			System.out.println("Expires in: "+authorizationCodeCredentials.getExpiresIn());
			
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accessToken = spotifyApi.getAccessToken();
		return spotifyApi.getAccessToken();
	}
	
	@GetMapping("/MMP/spotify/get_user_playlists")
	public PlaylistSimplified[] getSpotifyUserPlaylists() {
		PlaylistSimplified[] playlists = spotifyProxy.getUserPlaylists(accessToken, userId);
		return playlists;
	}
	
	@GetMapping("/MMP/spotify/get_playlist_tracks/playlist_id/{playlistId}")
	public Track[] getSpotifyPlaylistTracks(@PathVariable String playlistId) {
		Track[] tracks = spotifyProxy.getPlaylistTracks(accessToken, playlistId);
		return tracks;
	}
	
	@PostMapping("/MMP/spotify/add_tracks_to_playlist/playlist_id/{playlistId}")
	public String addTracksToSpotifyPlaylist(@PathVariable String playlistId, @RequestBody Track[] tracks) {
		String s = spotifyProxy.addTracksToPlaylist(accessToken, playlistId, tracks);
		return s;
	}
	
	@DeleteMapping("/MMP/spotify/remove_tracks_from_playlist/playlist_id/{playlistId}")
	public SnapshotResult removeTracksFromSpotifyPlaylist(@PathVariable String playlistId, @RequestBody Track[] tracks) {
		SnapshotResult result = spotifyProxy.removeTracksFromPlaylist(accessToken, playlistId, tracks);
		return result;
	}
	
	@PostMapping("/MMP/spotify/create_public_playlist/name/{name}")
	public Playlist createPublicPlaylist(@PathVariable("name") String name) {
		Playlist playlist = spotifyProxy.createPublicPlaylist(accessToken, userId, name);
		return playlist;
	}
	
	@PostMapping("/MMP/spotify/create_private_playlist/name/{name}")
	public Playlist createPrivatePlaylist(@PathVariable("name") String name) {
		Playlist playlist = spotifyProxy.createPrivatePlaylist(accessToken, userId, name);
		return playlist;
	}
	
}
