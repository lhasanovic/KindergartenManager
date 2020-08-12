package ba.unsa.etf.rpr;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ChildDiary {
	private Child child;
	private HashMap<LocalDateTime, DiaryEntry> diary;

	public ChildDiary(Child child) {
		this.child = child;
		diary = new HashMap<>();
	}

	public ChildDiary(Child child, HashMap<LocalDateTime, DiaryEntry> diary) {
		this.child = child;
		this.diary = diary;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public HashMap<LocalDateTime, DiaryEntry> getDiary() {
		return diary;
	}

	public void setDiary(HashMap<LocalDateTime, DiaryEntry> diary) {
		this.diary = diary;
	}

	public void addDiaryEntry(LocalDateTime timeDate, DiaryEntry entry) {
		diary.put(timeDate, entry);
	}
}
