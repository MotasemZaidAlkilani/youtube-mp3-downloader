package com.kilany.youtube_mp3.controller;

import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kilany.youtube_mp3.service.DownloadProgressService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ProgressController {
	
	@Autowired
	private DownloadProgressService downloadProgressService;

	@GetMapping("/progress")
	public Map<String, Integer> getProgress(HttpSession session) {
		int progress = downloadProgressService.getProgress(session);
		Map<String, Integer> response = new HashMap<>();
		response.put("progress", progress);
		return response;
	}
}
