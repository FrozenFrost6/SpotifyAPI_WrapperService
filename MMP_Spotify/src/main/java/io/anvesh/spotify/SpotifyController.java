package io.anvesh.spotify;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@RestController
@RequestMapping("/MMP/api")
public class SpotifyController {
	
	//My user id for spotify
	//private final String userId = "mqccjfqu3kbmd2u2t476b6nfb";
	
	
	@GetMapping("/get_user_playlists/access_token/{accessToken}/user_id/{userId}")
	public PlaylistSimplified[] getUserPlaylists(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId) {
		SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
		GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi.getListOfUsersPlaylists(userId).build();
		try {
			Paging<PlaylistSimplified> playlistPaging = getListOfUsersPlaylistsRequest.execute();
			return playlistPaging.getItems();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/get_playlist_tracks/access_token/{accessToken}/playlist_id/{playlistId}")
	public Track[] getPlaylistTracks(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId) {
		SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
		GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi.getPlaylistsItems(playlistId).build();
		try {
			Paging<PlaylistTrack> itemPaging = getPlaylistsItemsRequest.execute();
			PlaylistTrack[] pTracks = itemPaging.getItems();
			Track[] tracks = new Track[pTracks.length];
			for (int i = 0; i < pTracks.length; i++) {
				tracks[i] = (Track) pTracks[i].getTrack();
			}
			return tracks;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/search_tracks/access_token/{accessToken}/name/{trackName}")
	public Track[] searchTracks(@PathVariable("accessToken") String accessToken, @PathVariable("trackName") String trackName) {
		SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(trackName).build();
		try {
			Paging<Track> tracksPaging = searchTracksRequest.execute();
			Track[] tracks = tracksPaging.getItems();
			return tracks;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/add_tracks_to_playlist/access_token/{accessToken}/playlist_id/{playlistId}")
	public String addTracksToPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId, @RequestBody Track[] tracks) {
		ArrayList<Track> unfoundTracks = new ArrayList<>();
		ArrayList<Track> toAddTracks = new ArrayList<>();
		for (int i = 0; i < tracks.length; i++) {
			Track[] temp = searchTracks(accessToken, tracks[i].getName());
			if(temp.length == 0) {
				System.out.println("No song on spotify with name: "+tracks[i].getName());
				unfoundTracks.add(tracks[i]);
				continue;
			}
			Track t = temp[0];
			toAddTracks.add(t);
		}
		SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
		String[] trackURIs = new String[toAddTracks.size()];
		for (int i = 0; i < trackURIs.length; i++) {
			trackURIs[i] = toAddTracks.get(i).getUri();
		}
		AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(playlistId, trackURIs).build();
		try {
			SnapshotResult addedItems = addItemsToPlaylistRequest.execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		String s = "";
		s += unfoundTracks.size()+" Song(s) not found on Spotify: \n";
		for (int i = 0; i < unfoundTracks.size(); i++) {
			s += i+1 + ". "+unfoundTracks.get(i).getName()+"\n";
		}
		s += "\n"+toAddTracks.size()+" Song(s) added successfully";
		return s;
	}
	
	@DeleteMapping("/remove_tracks_from_playlist/access_token/{accessToken}/playlist_id/{playlistId}")
	public SnapshotResult removeTracksFromPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("playlistId") String playlistId, @RequestBody Track[] tracks) {
		SpotifyApi spotifyApi =  new SpotifyApi.Builder().setAccessToken(accessToken).build();
		// make the json array by making string first, big bruh moment, add playlist has inbuilt function but remove doesn't -_-
		String[] objectBuilderArray = new String[tracks.length];
		for (int i = 0; i < objectBuilderArray.length; i++) {
			objectBuilderArray[i] = "{\"uri\":"+"\""+tracks[i].getUri()+"\"}";
		}
		String finalOBS = "["+String.join(",", objectBuilderArray)+"]";

		JsonArray tracksToRemove = JsonParser.parseString(finalOBS).getAsJsonArray();
		RemoveItemsFromPlaylistRequest removeItemsFromPlaylistRequest = spotifyApi.removeItemsFromPlaylist(playlistId, tracksToRemove).build();
		try {
			SnapshotResult removedSnapshot = removeItemsFromPlaylistRequest.execute();
			return removedSnapshot;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@PostMapping("/create_public_playlist/access_token/{accessToken}/user_id/{userId}/name/{name}")
	public Playlist createPublicPlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId, @PathVariable("name") String name) {
		SpotifyApi spotifyApi =  new SpotifyApi.Builder().setAccessToken(accessToken).build();
		CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, name)
				.collaborative(false)
				.public_(true)
				.build();
		
		try {
			Playlist playlist = createPlaylistRequest.execute();
			return playlist;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@PostMapping("/create_private_playlist/access_token/{accessToken}/user_id/{userId}/name/{name}")
	public Playlist createPrivatePlaylist(@PathVariable("accessToken") String accessToken, @PathVariable("userId") String userId, @PathVariable("name") String name) {
		SpotifyApi spotifyApi =  new SpotifyApi.Builder().setAccessToken(accessToken).build();
		CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, name)
				.collaborative(false)
				.public_(false)
				.build();
		
		try {
			Playlist playlist = createPlaylistRequest.execute();
			return playlist;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	

}
