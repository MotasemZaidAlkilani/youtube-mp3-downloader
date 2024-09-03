package com.kilany.youtube_mp3.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class DownloadProgressService {
	   private static final String PROGRESS_ATTRIBUTE = "downloadProgress";

	    public void setProgress(HttpSession session, int progress) {
	        session.setAttribute(PROGRESS_ATTRIBUTE, progress);
	    }

	    public int getProgress(HttpSession session) {
	        Integer progress = (Integer) session.getAttribute(PROGRESS_ATTRIBUTE);
	        return progress != null ? progress : 0;
	    }

	    public void resetProgress(HttpSession session) {
	        session.removeAttribute(PROGRESS_ATTRIBUTE);
	    }
}
