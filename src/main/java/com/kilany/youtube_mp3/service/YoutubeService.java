package com.kilany.youtube_mp3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
public class YoutubeService {
	private static final Pattern YOUTUBE_URL_PATTERN = Pattern
			.compile("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$");

	private String getExecutableForOS(String fileName) throws IOException {
		String osName = System.getProperty("os.name").toLowerCase();
		String executable = fileName;
		if (osName.contains("win")) {
			executable += ".exe";
		}
		return getExecutablePath(executable);
	}

	private String getExecutablePath(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource("bin/" + fileName);
		File file = resource.getFile();
		return file.getAbsolutePath();
	}

	@PreAuthorize("hasRole('USER')")
	public String downloadAndConvert(String videoUrl) {

		if (!isValidYouTubeUrl(videoUrl)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid YouTube URL.");
		}

		try {
			String youtubeDlPath = getExecutableForOS("yt-dlp");

			String outputDir = System.getProperty("user.home") + "\\Downloads";

			File directory = new File(outputDir);

			if (!directory.exists())
				directory.mkdirs();

			String mp3FilePath = downloadmp3(youtubeDlPath, videoUrl, outputDir);

			return mp3FilePath;

		} catch (IOException | InterruptedException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request.", e);
		}
	}

	private boolean isValidYouTubeUrl(String url) {
		return YOUTUBE_URL_PATTERN.matcher(url).matches();
	}

	private String downloadmp3(String youtubeDlPath, String videoUrl, String outputDir)
			throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(youtubeDlPath, "-o", outputDir + "/%(title)s.%(ext)s",
				"--extract-audio", "--audio-format", "mp3", videoUrl);

		processBuilder.inheritIO();
		Process process = processBuilder.start();
		process.waitFor();

		File outputFolder = new File(outputDir);
		File[] files = outputFolder.listFiles((dir, name) -> name.endsWith(".mp3"));

		if (files != null && files.length > 0) {
			return files[0].getAbsolutePath();
		} else {
			throw new IOException("Failed to download video or video not found.");
		}
	}
}
