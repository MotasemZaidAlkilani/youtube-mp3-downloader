package com.kilany.youtube_mp3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kilany.youtube_mp3.service.YoutubeService;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class ConvertController {

	@Autowired
	private YoutubeService youtubeService;

	@PostMapping("/convert")
	@ResponseBody
	public ResponseEntity<byte[]> convert(@RequestParam("videoUrl") String videoUrl, HttpSession session) {

		try {
			String mp3FilePath = youtubeService.downloadAndConvert(videoUrl);

			File file = new File(mp3FilePath);

			if (!file.exists()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodeFilename(file.getName()));
			headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
			headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileContent.length));

			// Delete the file after it is sent to the client
			file.delete();

			return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String encodeFilename(String filename) throws UnsupportedEncodingException {
		return URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
	}
}
