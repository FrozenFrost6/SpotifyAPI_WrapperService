package io.anvesh.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

@FeignClient(name = "spotify-api-service", url = "localhost:8100")
public interface SpotifyProxy {
	
	@GetMapping("/MMP/api/get_user_playlists/access_token/{accessToken}/user_id/{userId}")
	public PlaylistSimplified[] getUserPlaylists(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId);
	
	@GetMapping("/MMP/api/get_playlist_tracks/access_token/{accessToken}/playlist_id/{playlistId}")
	public Track[] getPlaylistTracks(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId);
	
	@GetMapping("/MMP/api/search_tracks/access_token/{accessToken}/name/{trackName}")
	public Track[] searchTracks(@PathVariable("accessToken") String accessToken, @PathVariable("trackName") String trackName);
	
	@PostMapping("/MMP/api/add_tracks_to_playlist/access_token/{accessToken}/playlist_id/{playlistId}")
	public String addTracksToPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId, @RequestBody Track[] tracks);

	@DeleteMapping("/MMP/api/remove_tracks_from_playlist/access_token/{accessToken}/playlist_id/{playlistId}")
	public SnapshotResult removeTracksFromPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId, @RequestBody Track[] tracks);
	
	@PostMapping("/MMP/api/create_public_playlist/access_token/{accessToken}/user_id/{userId}/name/{name}")
	public Playlist createPublicPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId, @PathVariable("name") String name);
	
	@PostMapping("/MMP/api/create_private_playlist/access_token/{accessToken}/user_id/{userId}/name/{name}")
	public Playlist createPrivatePlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId, @PathVariable("name") String name);
	
}
