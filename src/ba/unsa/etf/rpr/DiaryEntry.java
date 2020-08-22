package ba.unsa.etf.rpr;

import java.time.LocalDateTime;

public class DiaryEntry {
	private LocalDateTime timeDate;
	private ChildActivity activity;
	private String description;

	public DiaryEntry() {}

	public DiaryEntry(LocalDateTime timeDate, ChildActivity activity, String description) {
		this.timeDate = timeDate;
		this.activity = activity;
		this.description = description;
	}

	public DiaryEntry(LocalDateTime timeDate, ChildActivity activity) {
		this.timeDate = timeDate;
		this.activity = activity;
	}

	public ChildActivity getActivity() {
		return activity;
	}

	public void setActivity(ChildActivity activity) {
		this.activity = activity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimeDate() {
		return timeDate;
	}

	public void setTimeDate(LocalDateTime timeDate) {
		this.timeDate = timeDate;
	}
}
